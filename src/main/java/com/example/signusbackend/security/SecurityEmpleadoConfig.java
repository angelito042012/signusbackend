package com.example.signusbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
public class SecurityEmpleadoConfig {

    //Falta implementar el AuthenticationProvider para empleados y todo lo que tienen los clientes


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para simplificar (habilitar en producción si es necesario)
            .securityMatcher("/sistema/**", "/api/auth/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/sistema").permitAll() // Página de login para empleados
                .requestMatchers("/sistema/**").hasAnyRole("ADMIN", "VENTAS", "ALMACEN", "PEDIDOS") // Rutas protegidas para empleados
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Rutas protegidas para administradores
                .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
            )
            .formLogin(form -> form
                .loginPage("/login/sistema") // Página personalizada para empleados
                .usernameParameter("email") // Campo para el correo
                .passwordParameter("password") // Campo para la contraseña
                .defaultSuccessUrl("/sistema/home", true) // Redirección después del login exitoso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL para cerrar sesión
                .logoutSuccessUrl("/login/sistema?logout") // Redirección después del logout
                .permitAll()
            );

        return http.build();
    }

}
