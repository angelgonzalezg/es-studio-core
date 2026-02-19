package com.esstudio.platform.esstudiocore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.esstudio.platform.esstudiocore.service.ProjectService;
import com.esstudio.platform.esstudiocore.dto.CreateProjectDto;

import jakarta.validation.Valid;

// @CrossOrigin(origins = {"http://localhost:3000"}) // Allow requests from the frontend application
// @CrossOrigin(originPatterns = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from frontend applications running on localhost:3000 and localhost:5173
@CrossOrigin(origins = "*", originPatterns = "*") // Allow requests from any origin (for development purposes only,
                                                  // consider restricting this in production)
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @PreAuthorize("hasAnyRole('ADMIN','DESIGNER')")
    @PostMapping("/create")
    public ResponseEntity<?> createProject(
            @RequestBody @Valid CreateProjectDto dto) {
        service.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable("id") long id) {
        service.deleteProject(id);
    }
}
