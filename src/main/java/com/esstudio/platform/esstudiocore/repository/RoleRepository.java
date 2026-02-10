package com.esstudio.platform.esstudiocore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esstudio.platform.esstudiocore.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
