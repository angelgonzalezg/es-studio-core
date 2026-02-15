package com.esstudio.platform.esstudiocore.service;

import java.util.List;
import java.util.Optional;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;

public interface UserService {

    List<UserDto> getUsers();

    Optional<UserDto> getUserById(long id);

    UserDto createUser(CreateUserDto dto);

    boolean existsByEmail(String email);

}
