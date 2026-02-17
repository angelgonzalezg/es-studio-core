package com.esstudio.platform.esstudiocore.service;

import java.util.List;
import java.util.Optional;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDetailsDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.security.enums.RoleType;

public interface UserService {

    Optional<UserDto> getUserById(long id);

    Optional<UserDto> getUserByUuid(String uuid);

    List<UserDto> getUsers();

    UserDto createUser(CreateUserDto dto);

    UserDetailsDto updateUser(Long id, UpdateUserDto dto);

    UserDetailsDto getUserDetails(Long id);

    void changeRole(Long id, RoleType newRole, Long currentAdminId);

    boolean existsByEmail(String email);

}
