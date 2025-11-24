package com.example.signusbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

                        

                        // ---------- PUBLIC ----------
                        .requestMatchers(HttpMethod.GET,"/").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias").permitAll()
                        .requestMatchers("/admin-login", "/admin-login.html", "/swagger-auth.js").permitAll()
                        .requestMatchers("/admin-register", "/admin-register.html").permitAll()

                        // AUTH
                        .requestMatchers("/api/auth/**").permitAll()

                        // Productos (clientes compran sin login)
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        
                        // Ventas
                        .requestMatchers("/api/ventas/**")
                            .hasAnyRole("VENTAS", "ADMIN")

                        // Pedidos
                        .requestMatchers("/api/pedidos/**")
                            .hasAnyRole("PEDIDOS", "ADMIN")

                        // Operaciones inventario
                        .requestMatchers("/api/operaciones-inventario/**")
                            .hasAnyRole("ALMACEN", "ADMIN")

                        // Movimientos inventario
                        .requestMatchers("/api/movimientos-inventario/**")
                            .hasAnyRole("ALMACEN", "ADMIN")

                        // Inventario general
                        .requestMatchers("/api/inventario/**")
                            .hasAnyRole("ALMACEN", "ADMIN")

                        

                        // Carritos (cliente y admin)
                        .requestMatchers("/api/carritos/**")
                            .hasAnyRole("CLIENTE", "ADMIN")

                        // Crear/editar productos
                        .requestMatchers(HttpMethod.POST, "/api/productos/**")
                            .hasAnyRole("ADMIN", "ALMACEN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**")
                            .hasAnyRole("ADMIN", "ALMACEN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**")
                            .hasAnyRole("ADMIN", "ALMACEN")



                        // Swagger (solo ADMIN)
                        .requestMatchers("/api/swagger-ui/**", "/api/docs/**").permitAll()//.hasRole("ADMIN") //ni modo, que mas le vamos a hacer
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()//.hasRole("ADMIN")


                        // Los roles de mas alto privilegio deben ir al final
                        // ---------- ADMIN (TOTAL ACCESO) ----------

                        // Métodos de pago
                        .requestMatchers("/api/metodos-pago/**")
                            .hasRole("ADMIN")

                        // Empleados
                        .requestMatchers("/api/empleados/**")
                            .hasRole("ADMIN")

                        // Clientes
                        .requestMatchers("/api/clientes/**")
                            .hasRole("ADMIN")

                        // Categorías
                        .requestMatchers("/api/categorias/**")
                            .hasRole("ADMIN")

                        // Usuarios empleados
                        .requestMatchers("/api/usuarios-empleados/**")
                            .hasRole("ADMIN")

                        // Usuarios clientes
                        .requestMatchers("/api/usuarios-clientes/**")
                            .hasRole("ADMIN")
                        

                        .requestMatchers(
                            "/api/empleados/**",
                            "/api/inventario/**",
                            "/api/metodos-pago/**",
                            "/api/clientes/**",
                            "/api/ventas/**",
                            "/api/productos/**"
                        ).hasRole("ADMIN")


                        // ----------- CUALQUIER OTRO ENDPOINT -----------
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
