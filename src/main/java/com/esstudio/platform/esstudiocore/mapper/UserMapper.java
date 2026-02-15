package com.esstudio.platform.esstudiocore.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.entities.User;


@Component
public class UserMapper {

    // CreateUserDto -> User (entity)
    public User toEntity(CreateUserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());

        return user;
    }

    // User (entity) -> UserDto (client response)
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setUuid(user.getUuid());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setEnabled(user.isEnabled());
        dto.setCreationTime(user.getCreationTime());
        dto.setUpdateTime(user.getUpdateTime());

        if (user.getRoles() != null) {
            dto.setRoles(
                    user.getRoles()
                            .stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toList()));
        }
        return dto;
    }

    // UpdateUserDto -> User
    public void updateEntity(User user, UpdateUserDto dto) {
        
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
}
