package com.esstudio.platform.esstudiocore.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
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

        // Password
        user.setRoles(roles);
        user.setPassword(
            passwordEncoder.encode(dto.getPassword())
        );

        user = repository.save(user);

        return userMapper.toDto(user);
    }

    // Validate if email exists in db 
    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
