package com.esstudio.platform.esstudiocore.service;

import java.util.List;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;

public interface UserService {

    List<UserDto> getUsers();

    UserDto createUser(CreateUserDto dto);

    boolean existsByEmail(String email);

}
