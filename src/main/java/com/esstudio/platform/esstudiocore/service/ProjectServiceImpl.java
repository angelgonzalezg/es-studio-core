package com.esstudio.platform.esstudiocore.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.esstudio.platform.esstudiocore.dto.CreateProjectDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;
import com.esstudio.platform.esstudiocore.entities.DesignerProfile;
import com.esstudio.platform.esstudiocore.entities.Project;
import com.esstudio.platform.esstudiocore.enums.ProjectStatus;

import com.esstudio.platform.esstudiocore.repository.ClientProfileRepository;
import com.esstudio.platform.esstudiocore.repository.DesignerProfileRepository;
import com.esstudio.platform.esstudiocore.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ClientProfileRepository clientRepository;

    @Autowired
    private DesignerProfileRepository designerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    @Transactional
    public void createProject(CreateProjectDto dto) {
        Project project = new Project();

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStatus(ProjectStatus.NEW.name());

        // Buscar clientes
        List<ClientProfile> clients =
            clientRepository.findAllById(dto.getClientIds());

        if(clients.size() != dto.getClientIds().size()){
            throw new RuntimeException("Some clients not found");
        }

        // Buscar diseñadores
        List<DesignerProfile> designers =
            designerRepository.findAllById(dto.getDesignerIds());

        if(designers.size() != dto.getDesignerIds().size()){
            throw new RuntimeException("Some designers not found");
        }

        // Asignar relaciones (bidireccional)
        for(ClientProfile client : clients){
            project.getClients().add(client);
            client.getProjects().add(project);
        }

        for(DesignerProfile designer : designers){
            project.getDesigners().add(designer);
            designer.getProjects().add(project);
        }

        projectRepository.save(project);
    }


    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found!"));

        // Break FK relationship with client profiles
        for (ClientProfile client : project.getClients()) {
            client.getProjects().remove(project);
        }

        // Break FK relationship with designer profiles
        for (DesignerProfile designer : project.getDesigners()) {
            designer.getProjects().remove(project);
        }

        project.getClients().clear();
        project.getDesigners().clear();

        projectRepository.delete(project);
    }
    
}
