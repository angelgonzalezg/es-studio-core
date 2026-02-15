package com.esstudio.platform.esstudiocore.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.esstudio.platform.esstudiocore.dto.ClientProfileDto;
import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;
import com.esstudio.platform.esstudiocore.entities.User;

@Component
public class UserMapper {

    // CreateUserDto -> User (entity)
    public User toEntity(CreateUserDto dto) {
        if (dto == null)
            return null;

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());

        return user;
    }

    // User (entity) -> UserDto (client response)
    public UserDto toDto(User user) {
        if (user == null)
            return null;

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setUuid(user.getUuid());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setEnabled(user.isEnabled());
        dto.setCreationTime(user.getCreationTime());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setRoles(
                    user.getRoles()
                            .stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toList()));
        }

        return dto;
    }

    // UpdateUserDto -> User
    public void updateEntity(
            User user,
            UpdateUserDto dto,
            ClientProfile clientProfile,
            ClientProfileDto clientProfileDto) {

        if (dto == null || user == null)
            return;

        // User fields
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

        // Client profile fields
        if (clientProfileDto != null && clientProfile != null) {

            if (clientProfileDto.getCompanyName() != null) {
                clientProfile.setCompanyName(clientProfileDto.getCompanyName());
            }

            if (clientProfileDto.getTaxId() != null) {
                clientProfile.setTaxId(clientProfileDto.getTaxId());
            }

            if (clientProfileDto.getBillingAddress() != null) {
                clientProfile.setBillingAddress(clientProfileDto.getBillingAddress());
            }

            user.setClientProfile(clientProfile);
        }
    }
}
