package com.esstudio.platform.esstudiocore.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esstudio.platform.esstudiocore.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findUserByEmail(String email);

        Optional<User> findUserByUuid(UUID uuid);

        @Query("""
                        SELECT u FROM User u
                        LEFT JOIN FETCH u.roles
                        LEFT JOIN FETCH u.clientProfile
                        LEFT JOIN FETCH u.designerProfile
                        WHERE u.id = :id
                        """)
        Optional<User> findUserWithProfilesById(@Param("id") Long id);

        @Query("""
                         SELECT COUNT(u)
                         FROM User u
                         JOIN u.roles r
                         WHERE r.name = :role
                        """)
        long countUsersWithRole(@Param("role") String role);

        boolean existsUserByEmail(String email);
}
