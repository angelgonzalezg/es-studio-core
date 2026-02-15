package com.esstudio.platform.esstudiocore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;
import com.esstudio.platform.esstudiocore.entities.Role;
import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.mapper.UserMapper;
import com.esstudio.platform.esstudiocore.repository.RoleRepository;
import com.esstudio.platform.esstudiocore.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    // Get all Users
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        return repository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(long id) {
        return repository.findById(id)
                .map(userMapper::toDto);
    }

    // Create User
    @Override
    @Transactional
    public UserDto createUser(CreateUserDto dto) {

        // DTO -> Entity
        User user = userMapper.toEntity(dto);

        // Default roles
        List<Role> roles = new ArrayList<>();

        roleRepository.findByName("ROLE_CLIENT")
                .ifPresent(roles::add);

        if (user.isAdmin()) {
            roleRepository.findByName("ROLE_ADMIN")
                    .ifPresent(roles::add);
        }

        // Set roles to user
        user.setRoles(roles);

        // Password
        user.setPassword(
            passwordEncoder.encode(dto.getPassword())
        );

        if (dto.getClientProfile() != null) {
            // If client profile data is provided, create a client profile and associate it with the user
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setCompanyName(dto.getClientProfile().getCompanyName());
            clientProfile.setTaxId(dto.getClientProfile().getTaxId());
            clientProfile.setBillingAddress(dto.getClientProfile().getBillingAddress());
            clientProfile.setUser(user); // Set the relationship
            // Save the client profile (cascading will save the user as well)
            user.setClientProfile(clientProfile);
        }

        user = repository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUserProfile(Long id, UpdateUserDto dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        ClientProfile clientProfile = user.getClientProfile();

        if (clientProfile == null && dto.getClientProfile() != null) {
            clientProfile = new ClientProfile();
            clientProfile.setUser(user);
        }

        // Delegate the update logic to the mapper
        userMapper.updateEntity(user, dto, clientProfile, dto.getClientProfile());

        user.setUpdateTime(LocalDateTime.now());

        user = repository.save(user);
        return userMapper.toDto(user);  

    }

    // Validate if email exists in db 
    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
