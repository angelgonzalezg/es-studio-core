package com.esstudio.platform.esstudiocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esstudio.platform.esstudiocore.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    

}
