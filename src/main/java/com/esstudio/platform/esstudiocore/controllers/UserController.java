package com.esstudio.platform.esstudiocore.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esstudio.platform.esstudiocore.dto.CreateUserDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.service.UserService;

import jakarta.validation.Valid;

// @CrossOrigin(origins = {"http://localhost:3000"}) // Allow requests from the frontend application
// @CrossOrigin(originPatterns = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from frontend applications running on localhost:3000 and localhost:5173
@CrossOrigin(origins = "*", originPatterns = "*") // Allow requests from any origin (for development purposes only,
                                                  // consider restricting this in production)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('DESIGNER')")
    @GetMapping
    public List<UserDto> list() {
        return service.getUsers();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DESIGNER')")
    @GetMapping("/{id}")
    public Optional<UserDto> getById(@PathVariable("id") long id) {
        return service.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DESIGNER')")
    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserDto user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validate(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(user));
    }

    @PutMapping("/{id}/profile")
    public void update(@PathVariable("id") long id, @RequestBody UpdateUserDto user) {
        service.updateUserProfile(id, user);
    }

    private ResponseEntity<?> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "The " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
