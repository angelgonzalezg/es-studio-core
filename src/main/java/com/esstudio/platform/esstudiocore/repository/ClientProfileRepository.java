package com.esstudio.platform.esstudiocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esstudio.platform.esstudiocore.entities.ClientProfile;
import java.util.Optional;


public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    // Optional<ClientProfile> findByUserId(Long id);

    // @Query("SELECT u FROM User u LEFT JOIN FETCH u.clientProfile WHERE u.id = :id")
    // Optional<ClientProfile> findByIdWithDetails(@Param("id") Long id);

    
}
