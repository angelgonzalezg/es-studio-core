package com.esstudio.platform.esstudiocore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

        return userMapper.toDetailsDto(user);
    }

    @Override
    @Transactional
    public UserDetailsDto updateUser(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        userMapper.updateEntity(user, dto);

        // Client profile update
        if (roleService.hasRole(user, RoleType.ROLE_USER)
                || roleService.hasRole(user, RoleType.ROLE_ADMIN) && dto.getClientProfile() != null) {
            clientProfileService.updateProfile(user, dto.getClientProfile());
        }

        // Designer profile update
        if (roleService.hasRole(user, RoleType.ROLE_DESIGNER)
                || roleService.hasRole(user, RoleType.ROLE_ADMIN) && dto.getDesignerProfile() != null) {
            designerProfileService.updateProfile(user, dto.getDesignerProfile());
        }

        user.setUpdateTime(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return userMapper.toDetailsDto(savedUser);
    }

    @Override
    @Transactional
    public void changeRole(Long id, RoleType newRole, Long currentAdminId) {

        User targetUser = userRepository.findById(id)
                .orElseThrow();

        if (currentAdminId.equals(targetUser.getId()) && newRole != RoleType.ROLE_ADMIN) {
            throw new IllegalStateException("Admins cannot demote themselves!");
        }

        if (roleService.hasRole(targetUser, RoleType.ROLE_ADMIN) && newRole != RoleType.ROLE_ADMIN) {
            long adminCount = userRepository.countUsersWithRole(RoleType.ROLE_ADMIN.name());
            if (adminCount <= 1) {
                throw new IllegalStateException("Cannot demote the last admin user!");
            }
        }

        targetUser.getRoles().clear(); // Clear existing roles

        roleService.assignRoletoUser(targetUser, newRole); // Assign new role

        targetUser.setUpdateTime(LocalDateTime.now());
        userRepository.save(targetUser);
    }

    @Override
    public Optional<UserDto> getUserByUuid(String uuid) {
        return userRepository.findUserByUuid(UUID.fromString(uuid))
                .map(userMapper::toDto);
    }

    // Validate if email exists in db
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }
}
