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
import com.esstudio.platform.esstudiocore.service.UserService;
import com.esstudio.platform.esstudiocore.security.TokenBlacklist;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import com.esstudio.platform.esstudiocore.dto.AuthResponseDto;
import com.esstudio.platform.esstudiocore.dto.CreateUserDto;

import static com.esstudio.platform.esstudiocore.security.TokenJwtConfig.*;

// @CrossOrigin(origins = {"http://localhost:3000"}) // Allow requests from the frontend application
// @CrossOrigin(originPatterns = {"http://localhost:3000", "http://localhost:5173"}) // Allow requests from frontend applications running on localhost:3000 and localhost:5173
@CrossOrigin(origins = "*", originPatterns = "*") // Allow requests from any origin (for development purposes only, consider restricting this in production)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserDto dto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validate(result);
        }

        dto.setAdmin(false);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.getUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userSpringSec = (UserDetails) authentication.getPrincipal();
            String uuid = userSpringSec.getUsername();

            Collection<String> roles = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

            Claims claims = Jwts.claims()
                .add("authorities", roles)
                .build();

            String token = Jwts.builder()
                .subject(uuid)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 1800000)) // 30 min
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();

            Map<String, Object> tokenData = new LinkedHashMap<>();
            tokenData.put("token", token);
            tokenData.put("type", "Bearer");
            tokenData.put("expires_in", 1800);

            AuthResponseDto<Object> responseDTO = new AuthResponseDto<>();
            responseDTO.setMessage(String.format("Successfully authenticated user %s", uuid));
            responseDTO.setData(tokenData);
            responseDTO.setTimestamp(LocalDateTime.now());
            responseDTO.setError(null);

            return ResponseEntity.ok(responseDTO);
        } catch (AuthenticationException e) {
            AuthResponseDto<Object> responseDTO = new AuthResponseDto<>();
            responseDTO.setMessage("Authentication failed!");
            responseDTO.setData(null);
            responseDTO.setTimestamp(LocalDateTime.now());
            responseDTO.setError(e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
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

    private ResponseEntity<?> validate(BindingResult result){
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "The " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}