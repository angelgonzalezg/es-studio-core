package com.esstudio.platform.esstudiocore.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.service.AuthService;
import com.esstudio.platform.esstudiocore.service.UserService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import com.esstudio.platform.esstudiocore.dto.AuthDto;
import com.esstudio.platform.esstudiocore.dto.CreateUserDto;

// @CrossOrigin(origins = {"http://localhost:3000"}) // Allow requests from the frontend application
// @CrossOrigin(originPatterns = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from frontend applications running on localhost:3000 and localhost:5173
@CrossOrigin(origins = "*", originPatterns = "*") // Allow requests from any origin (for development purposes only, consider restricting this in production)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserDto dto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validate(result);
        }

        dto.setAdmin(false);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        try {
            return ResponseEntity.ok(
                authService.login(loginRequest)
            );

        } catch (Exception e) {

            AuthDto<Object> response = new AuthDto<>();

            response.setMessage("Authentication failed!");
            response.setData(null);
            response.setTimestamp(LocalDateTime.now());
            response.setError(e.getMessage());

            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            authService.logout(token);
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> validate(BindingResult result){
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "The " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}