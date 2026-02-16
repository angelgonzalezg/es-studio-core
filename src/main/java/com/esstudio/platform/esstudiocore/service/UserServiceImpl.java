package com.esstudio.platform.esstudiocore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDetailsDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;
import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.mapper.UserMapper;
import com.esstudio.platform.esstudiocore.repository.UserRepository;
import com.esstudio.platform.esstudiocore.security.enums.RoleType;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ClientProfileService clientProfileService;

    @Autowired
    private DesignerProfileService designerProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    // Get all Users
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    // Create User
    @Override
    @Transactional
    public UserDto createUser(CreateUserDto dto) {

        // DTO -> Entity
        User user = userMapper.toEntity(dto);

        // Password
        user.setPassword(
                passwordEncoder.encode(dto.getPassword()));

        // Save user first to generate ID
        User savedUser = userRepository.save(user);

        // Always assign default ROLE_USER for new users
        roleService.assignRoletoUser(savedUser, RoleType.ROLE_USER);

        // If client profile data is provided, create and associate client profile
        if (dto.getClientProfile() != null) {
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setCompanyName(dto.getClientProfile().getCompanyName());
            clientProfile.setTaxId(dto.getClientProfile().getTaxId());
            clientProfile.setBillingAddress(dto.getClientProfile().getBillingAddress());
            clientProfile.setUser(savedUser);
            savedUser.setClientProfile(clientProfile);
            userRepository.save(savedUser); // Save to persist relationship
        } else {
            userRepository.save(savedUser); // Save to persist roles
        }

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsDto getUserDetails(Long id) {

        User user = userRepository.findUserWithProfilesById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDetails(user);
    }

    @Override
    @Transactional
    public UserDetailsDto updateUser(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // boolean isAdmin = auth.getAuthorities()
        //         .stream()
        //         .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        userMapper.updateEntity(user, dto);

        // // Update roles if provided
        // if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
        //     if (!isAdmin) {
        //         throw new RuntimeException("Only admins can update roles!");
        //     }
        //     for (String roleName : dto.getRoles()) {
        //         RoleType roleType = RoleType.valueOf(roleName);
        //         roleService.assignRoletoUser(user, roleType);
        //     }
        // }

        // Client profile update
        if (roleService.hasRole(user, RoleType.ROLE_USER) || roleService.hasRole(user, RoleType.ROLE_ADMIN) && dto.getClientProfile() != null) {
            clientProfileService.updateProfile(user, dto.getClientProfile());
        }

        // Designer profile update
        if (roleService.hasRole(user, RoleType.ROLE_DESIGNER) || roleService.hasRole(user, RoleType.ROLE_ADMIN) && dto.getDesignerProfile() != null) {
            designerProfileService.updateProfile(user, dto.getDesignerProfile());
        }

        user.setUpdateTime(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return userMapper.toDetails(savedUser);
    }

    public void promoteUser(Long id, RoleType newRole) {  
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Only admins can promote users!");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        
        user.getRoles().clear(); // Clear existing roles


        roleService.assignRoletoUser(user, RoleType.ROLE_USER); // Assign default role
        roleService.assignRoletoUser(user, newRole); // Assign new role

        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
    }

    // Validate if email exists in db
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }
}
