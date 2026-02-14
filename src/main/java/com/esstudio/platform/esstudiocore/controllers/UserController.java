package com.esstudio.platform.esstudiocore.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.service.UserService;

import jakarta.validation.Valid;
import com.esstudio.platform.esstudiocore.security.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
// @CrossOrigin(origins = {"http://localhost:3000"}) // Allow requests from the frontend application
// @CrossOrigin(originPatterns = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from frontend applications running on localhost:3000 and localhost:5173
@CrossOrigin(origins = "*", originPatterns = "*") // Allow requests from any origin (for development purposes only, consider restricting this in production)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DESIGNER')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) { 
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
        user.setAdmin(false);
        return create(user, result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            tokenBlacklist.blacklistToken(token);
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "The field " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


}
