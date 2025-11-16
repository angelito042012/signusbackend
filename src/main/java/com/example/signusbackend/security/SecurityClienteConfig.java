package com.example.signusbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.signusbackend.auth.cliente.ClienteUserDetailsService;

@Configuration
@Order(1)
public class SecurityClienteConfig {

    private final PasswordEncoder passwordEncoder;

    private final ClienteUserDetailsService clienteUserDetailsService;

    public SecurityClienteConfig(ClienteUserDetailsService clienteUserDetailsService, PasswordEncoder passwordEncoder) {
        this.clienteUserDetailsService = clienteUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Bean
    public SecurityFilterChain clienteSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para simplificar (habilitar en producción si es necesario)
            .securityMatcher("/cliente/**", "/api/auth/**") // Aplica esta configuración solo a estas rutas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register-cliente", "/api/auth/login-cliente").permitAll() // Rutas públicas para clientes
                .requestMatchers("/cliente/public/**").permitAll() // Rutas públicas adicionales
                .anyRequest().hasRole("CLIENTE") // Otras rutas requieren el rol CLIENTE
            )
            .formLogin(form -> form
                .loginPage("/cliente/login") // Página personalizada para clientes
                .usernameParameter("email") // Campo para el correo
                .passwordParameter("password") // Campo para la contraseña
                .defaultSuccessUrl("/cliente/home", true) // Redirección después del login exitoso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/cliente/logout") // URL para cerrar sesión
                .logoutSuccessUrl("/cliente/login?logout") // Redirección después del logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager clienteAuthenticationManager(HttpSecurity http) throws Exception {
        return http
            .getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(clienteUserDetailsService)
            .passwordEncoder(this.passwordEncoder) // Usa el bean definido en CommonSecurityConfig
            .and()
            .build();
    }

}
