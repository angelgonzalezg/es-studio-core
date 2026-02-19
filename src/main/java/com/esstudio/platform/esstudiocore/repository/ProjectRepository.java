package com.esstudio.platform.esstudiocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esstudio.platform.esstudiocore.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>  {
    
}
