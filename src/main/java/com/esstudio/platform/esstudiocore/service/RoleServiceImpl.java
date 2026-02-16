package com.esstudio.platform.esstudiocore.service;

import com.esstudio.platform.esstudiocore.entities.Role;
import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.repository.RoleRepository;
import com.esstudio.platform.esstudiocore.security.enums.RoleType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void assignRoletoUser(User user, RoleType type) {
        Role role = roleRepository.findByName(type.name())
                .orElseThrow(() -> new RuntimeException("Role not found: " + type.name()));

        user.getRoles().add(role);
    }

    @Override
    public boolean hasRole(User user, RoleType type) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(type.name()));
    }

}