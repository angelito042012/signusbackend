package com.example.signusbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.signusbackend.auth.cliente.ClienteUserDetails;
import com.example.signusbackend.auth.cliente.ClienteUserDetailsService;

@Component
public class ClienteAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private ClienteUserDetailsService clienteUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Aca debes de tener mucho cuidado con los imports

    public ClienteAuthenticationProvider(ClienteUserDetailsService clienteUserDetailsService, PasswordEncoder passwordEncoder) {
        this.clienteUserDetailsService = clienteUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        ClienteUserDetails userDetails = (ClienteUserDetails) clienteUserDetailsService.loadUserByUsername(email);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
