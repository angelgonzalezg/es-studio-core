package com.esstudio.platform.esstudiocore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esstudio.platform.esstudiocore.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH u.clientProfile
            LEFT JOIN FETCH u.designerProfile
            WHERE u.id = :id
            """)
    Optional<User> findUserWithProfilesById(
            @Param("id") Long id);

    boolean existsUserByEmail(String email);
}
