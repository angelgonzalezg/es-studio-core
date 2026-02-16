package com.esstudio.platform.esstudiocore.service;

import java.util.List;
import java.util.Optional;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDetailsDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.security.enums.RoleType;

public interface UserService {

    List<UserDto> getUsers();

    Optional<UserDto> getUserById(long id);

    UserDto createUser(CreateUserDto dto);

    UserDetailsDto updateUser(Long id, UpdateUserDto dto);

    UserDetailsDto getUserDetails(Long id);

    void promoteUser(Long id, RoleType newRole);

    boolean existsByEmail(String email);

}
