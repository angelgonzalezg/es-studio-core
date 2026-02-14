package com.esstudio.platform.esstudiocore.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.esstudio.platform.esstudiocore.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static com.esstudio.platform.esstudiocore.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User user = null;
        String email = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            email = user.getEmail();
            password = user.getPassword();
        } catch (JacksonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        // Cast the principal to Spring Security's User to get the username (uuid)
        org.springframework.security.core.userdetails.User userSpringSec = (org.springframework.security.core.userdetails.User) authResult
                .getPrincipal();
        String uuid = userSpringSec.getUsername();

        Collection<String> roles = authResult.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                // .filter(role -> role.startsWith("ROLE_"))
                .toList();
        
        System.out.println(roles);

        Claims claims = Jwts.claims()
                // .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("authorities", roles)
                .build();

        String token = Jwts.builder()
                .subject(uuid)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 1800000)) // Token expires in 30min
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
        
        System.out.println(claims.getIssuedAt());

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("token", token);
        body.put("type", "Bearer");
        body.put("expires_in", 1800);
        body.put("message", String.format("Successfully authenticated user %s", uuid));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Authentication failed!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
