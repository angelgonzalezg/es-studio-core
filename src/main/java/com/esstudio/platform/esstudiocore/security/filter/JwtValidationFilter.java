package com.esstudio.platform.esstudiocore.security.filter;

import static com.esstudio.platform.esstudiocore.security.TokenJwtConfig.CONTENT_TYPE;
import static com.esstudio.platform.esstudiocore.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.esstudio.platform.esstudiocore.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.esstudio.platform.esstudiocore.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.esstudio.platform.esstudiocore.security.TokenBlacklist;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    private final TokenBlacklist tokenBlacklist;

    public JwtValidationFilter(AuthenticationManager authenticationManager, TokenBlacklist tokenBlacklist) {
        super(authenticationManager);
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        // Extract Jwt token from header
        String token = header.replace(PREFIX_TOKEN, "");

        if (tokenBlacklist.isBlacklisted(token)) {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);

            Map<String, String> body = Map.of(
                    "error", "Token has been blacklisted",
                    "message", "Please login again");

            response.getWriter()
                    .write(new ObjectMapper().writeValueAsString(body));

            return;
        }

        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();

            String username = claims.getSubject();
            List<?> roles = claims.get("authorities", List.class);

            if (roles == null) {
                roles = List.of();
            }

            // Convert the roles from the claims to a list of GrantedAuthority objects
            Collection<? extends GrantedAuthority> authorities = roles.stream()
                    .map(Object::toString)
                    .filter(role -> role.startsWith("ROLE_"))
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            // Clear any existing authentication
            SecurityContextHolder.clearContext();

            // Create an Authentication object with the user UUID and authorities
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    null, authorities);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Continue with the filter chain
            chain.doFilter(request, response);

        } catch (JwtException e) {

            SecurityContextHolder.clearContext();

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);

            Map<String, String> body = Map.of(
                    "error", "Invalid or expired token",
                    "message", e.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        }

    }

}
