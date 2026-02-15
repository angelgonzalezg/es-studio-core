package com.esstudio.platform.esstudiocore.service;

import static com.esstudio.platform.esstudiocore.security.TokenJwtConfig.SECRET_KEY;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.esstudio.platform.esstudiocore.dto.AuthDto;
import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.security.TokenBlacklist;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Override
    public AuthDto<?> login(User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        UserDetails userSpringSec =
            (UserDetails) authentication.getPrincipal();

        String uuid = userSpringSec.getUsername();

        Collection<String> roles = authentication.getAuthorities()
            .stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.toList());

        Claims claims = Jwts.claims()
            .add("authorities", roles)
            .build();

        String token = Jwts.builder()
            .subject(uuid)
            .claims(claims)
            .expiration(new Date(System.currentTimeMillis() + 1800000))
            .issuedAt(new Date())
            .signWith(SECRET_KEY)
            .compact();

        Map<String, Object> tokenData = new LinkedHashMap<>();

        tokenData.put("token", token);
        tokenData.put("type", "Bearer");
        tokenData.put("expires_in", 1800);

        AuthDto<Object> response = new AuthDto<>();

        response.setMessage("Successfully authenticated user " + uuid);
        response.setData(tokenData);
        response.setTimestamp(LocalDateTime.now());
        response.setError(null);

        return response;
    }

    @Override
    public void logout(String token) {
        tokenBlacklist.blacklistToken(token);
    }
    
}
