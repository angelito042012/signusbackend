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
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/productos/**").permitAll()
                        .requestMatchers("/api/categorias/**").permitAll()
                        .requestMatchers("/admin-login", "/admin-login.html", "/swagger-auth.js").permitAll()
                        .requestMatchers("/admin-register", "/admin-register.html").permitAll()
                        //.requestMatchers("/api/auth/register/admin").permitAll() // Permitir acceso público
                        //.requestMatchers("/api/auth/login/admin").permitAll()



                        // ---------- CLIENTE ----------
                        .requestMatchers("/api/carrito/**").hasRole("CLIENTE")
                        .requestMatchers("/api/ventas/online/**").hasRole("CLIENTE")



                        /*.requestMatchers(HttpMethod.GET, "/api/ventas/**").hasAnyRole("ADMIN", "VENTAS", "PEDIDOS")
                        .requestMatchers(HttpMethod.POST, "/api/ventas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/ventas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/ventas/**").hasRole("ADMIN")*/

                        

                        // ---------- VENTAS ----------
                        .requestMatchers(
                            "/api/ventas/**",
                            "/api/clientes/**",
                            "/api/metodos-pago/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_VENTAS")


                        
                        

                        // ---------- ALMACEN ----------
                        .requestMatchers(
                            "/api/inventario/**",
                            "/api/productos/**"
                        ).hasAnyRole("ADMIN", "ALMACEN")




                        // ----------- SWAGGER -----------
                        .requestMatchers(
                            "/api/ventas/**"
                        ).hasAnyRole("ADMIN", "PEDIDOS")


                        // Swagger (solo ADMIN)
                        .requestMatchers("/api/swagger-ui/**", "/api/docs/**").permitAll()//.hasRole("ADMIN") //ni modo, que mas le vamos a hacer
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()//.hasRole("ADMIN")


                        // Los roles de mas alto privilegio deben ir al final
                        // ---------- ADMIN (TOTAL ACCESO) ----------
                        .requestMatchers(
                            "/api/empleados/**",
                            "/api/inventario/**",
                            "/api/metodos-pago/**",
                            "/api/clientes/**",
                            "/api/ventas/**"
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
