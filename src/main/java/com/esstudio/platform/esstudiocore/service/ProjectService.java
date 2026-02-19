package com.esstudio.platform.esstudiocore.service;

import com.esstudio.platform.esstudiocore.dto.CreateProjectDto;

public interface ProjectService {

    void createProject(CreateProjectDto dto);

    void deleteProject(Long id);

}
