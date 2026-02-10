package com.esstudio.platform.esstudiocore.service;

import java.util.List;

import com.esstudio.platform.esstudiocore.entities.User;

public interface UserService {

    List<User> findAll();

    User save(User user);

    boolean existsByEmail(String email);
}
