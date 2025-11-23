package com.example.signusbackend.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticatorFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticatorFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // NO filtrar login admin ni otros públicos
        return 
            path.equals("/api/auth/register/admin")
            || path.equals("/api/auth/login/admin")
            || path.equals("/admin-login")
            || path.equals("/admin-register")
            || path.equals("/admin-login.html")
            || path.equals("/admin-register.html")
            || path.equals("/swagger-auth.js");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("Processing JWT authentication filter");
        
        String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization Header: " + authHeader);

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.isTokenValid(token)) {
                    String email = jwtUtil.extractEmail(token);
                    String role = jwtUtil.extractRole(token);

                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                email,       // principal
                                null,
                                List.of(new SimpleGrantedAuthority(/*"ROLE_" + */role))
                        );
                        System.out.println("Rol extraído del token: " + role);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            // Manejo de errores de JWT
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido o expirado");
            return; // Detener la cadena de filtros
        }

        filterChain.doFilter(request, response);

    }
}
