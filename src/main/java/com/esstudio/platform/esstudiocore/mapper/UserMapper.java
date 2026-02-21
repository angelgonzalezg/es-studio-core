package com.esstudio.platform.esstudiocore.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDetailsDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.entities.User;

@Component
public class UserMapper {

    @Autowired
    private ClientProfileMapper clientMapper;

    @Autowired
    private DesignerProfileMapper designerMapper;

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

    // UpdateUserDto -> User (entity)
    public void updateEntity(
            User user,
            UpdateUserDto dto) {

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

        dto.setRoles(
                user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()));
        return dto;
    }

    // User (entity) -> UserDetailsDto (client response)
    public UserDetailsDto toDetailsDto(User user) {

        if (user == null)
            return null;

        UserDetailsDto dto = new UserDetailsDto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());

        dto.setRoles(
                user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()));

        if (user.getClientProfile() != null) {
            dto.setClientProfile(
                    clientMapper.toDto(
                            user.getClientProfile()));
        }

        if (user.getDesignerProfile() != null) {
            dto.setDesignerProfile(
                    designerMapper.toDto(
                            user.getDesignerProfile()));
        }

        return dto;
    }
}
