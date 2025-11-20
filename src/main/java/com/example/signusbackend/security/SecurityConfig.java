package com.example.signusbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import com.example.signusbackend.security.jwt.JwtAuthenticatorFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    final AppUserDetailsService userDetailsService;
    final JwtAuthenticatorFilter jwtAuthenticatorFilter;

    public SecurityConfig(AppUserDetailsService userDetailsService, JwtAuthenticatorFilter jwtAuthenticatorFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticatorFilter = jwtAuthenticatorFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(cs -> cs.disable())
                .cors(cs -> cs.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        //.requestMatchers("/api/auth/register-admin").permitAll() // Permitir acceso público
                        //.requestMatchers("/api/auth/login-admin").permitAll()
                        .requestMatchers("/api/productos/**").permitAll()
                        .requestMatchers("/api/categorias/**").permitAll()
                        .requestMatchers("/admin-login", "/admin-login.html", "/swagger-auth.js").permitAll()

                        // Swagger (solo ADMIN)
                        .requestMatchers("/api/swagger-ui/**", "/api/docs/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")

                        // Cliente
                        .requestMatchers("/api/carrito/**").hasRole("CLIENTE")
                        .requestMatchers("/api/ventas/online/**").hasRole("CLIENTE")

                        // Empleados
                        .requestMatchers("/sistema/**").hasAnyRole(
                                "ADMIN", "VENTAS", "ALMACEN", "PEDIDOS"
                        )


                        .anyRequest().authenticated()
                )

                // OAuth2 (lo terminamos cuando configuramos Google)
                /*.oauth2Login(oauth -> oauth
                        .loginPage("/oauth/login")
                );*/

                .authenticationProvider(authProvider())

                // Agregar filtro JWT
                .addFilterBefore(jwtAuthenticatorFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    //Provider de autenticación (Usuarios de BD)
    @Bean
        public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
        }

    //Encriptacion
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
