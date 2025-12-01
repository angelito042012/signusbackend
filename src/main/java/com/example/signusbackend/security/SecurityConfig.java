package com.example.signusbackend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        

                        // ---------- PUBLIC ----------

                        .requestMatchers(HttpMethod.GET,"/").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/{idProducto}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias").permitAll()
                        .requestMatchers("/admin-login", "/admin-login.html", "/swagger-auth.js").permitAll()
                        .requestMatchers("/admin-register", "/admin-register.html").permitAll()

                        // AUTH
                        .requestMatchers("/api/auth/**").permitAll()

                        // Productos (clientes compran sin login)
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()

                        // Ventas
                        .requestMatchers(HttpMethod.GET, "/api/ventas")
                            .hasAnyRole("PEDIDOS", "VENTAS", "ADMIN") // Listar todas las ventas
                        .requestMatchers(HttpMethod.GET, "/api/ventas/{idVenta}")
                            .hasAnyRole("PEDIDOS", "VENTAS", "ADMIN") // Obtener una venta por ID
                        .requestMatchers(HttpMethod.PUT, "/api/ventas/{idVenta}")
                            .hasAnyRole("VENTAS", "ADMIN") // Actualizar una venta existente
                        .requestMatchers(HttpMethod.DELETE, "/api/ventas/{idVenta}")
                            .hasAnyRole("VENTAS", "ADMIN") // Eliminar una venta
                        .requestMatchers(HttpMethod.GET, "/api/ventas/{idVenta}/detalles")
                            .hasAnyRole("PEDIDOS", "VENTAS", "ADMIN") // Listar detalles de una venta
                        .requestMatchers(HttpMethod.POST, "/api/ventas/{idVenta}/detalles")
                            .hasAnyRole("VENTAS", "ADMIN") // Agregar un detalle a una venta
                        .requestMatchers(HttpMethod.POST, "/api/ventas/registrar")
                            .hasAnyRole("VENTAS", "ADMIN") // Registrar una nueva venta física
                        .requestMatchers(HttpMethod.DELETE, "/api/ventas/{idVenta}/detalles/{idDetalle}")
                            .hasAnyRole("VENTAS", "ADMIN") // Eliminar un detalle de una venta

                        // Pedidos
                        .requestMatchers(HttpMethod.GET, "/api/pedidos")
                            .hasAnyRole("PEDIDOS", "ADMIN") // Listar todos los pedidos
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/{idPedido}")
                            .hasAnyRole("CLIENTE", "PEDIDOS", "ADMIN") // Obtener un pedido por ID
                        .requestMatchers(HttpMethod.PUT, "/api/pedidos/{idPedido}")
                            .hasAnyRole("PEDIDOS", "ADMIN") // Modificar un pedido
                        .requestMatchers(HttpMethod.DELETE, "/api/pedidos/{idPedido}")
                            .hasAnyRole("PEDIDOS", "ADMIN") // Eliminar un pedido
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/crear-desde-venta")
                            .hasAnyRole("PEDIDOS", "ADMIN") // Crear un pedido desde una venta
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/{idPedido}/estado")
                            .hasAnyRole("PEDIDOS", "ADMIN") // Modificar el estado de un pedido
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/estado/{estado}")
                            .hasAnyRole("PEDIDOS", "ADMIN") // Listar pedidos por estado
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/cliente/email/{email}")
                            .hasAnyRole("CLIENTE", "PEDIDOS", "ADMIN") // Listar pedidos por email del cliente
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/{idPedido}/venta/detalles")
                            .hasAnyRole("CLIENTE", "PEDIDOS", "ADMIN") // Obtener detalles de venta de un pedido

                        // Operaciones de inventario
                        .requestMatchers(HttpMethod.GET, "/api/operaciones-inventario/{id}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Obtener una operación de inventario por ID
                        .requestMatchers(HttpMethod.PUT, "/api/operaciones-inventario/{id}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Actualizar una operación de inventario
                        .requestMatchers(HttpMethod.DELETE, "/api/operaciones-inventario/{id}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Eliminar una operación de inventario
                        .requestMatchers(HttpMethod.GET, "/api/operaciones-inventario/detalles/{idDetalle}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Obtener un detalle de operación por ID
                        .requestMatchers(HttpMethod.PUT, "/api/operaciones-inventario/detalles/{idDetalle}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Actualizar un detalle de operación de inventario
                        .requestMatchers(HttpMethod.DELETE, "/api/operaciones-inventario/detalles/{idDetalle}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Eliminar un detalle de operación de inventario
                        .requestMatchers(HttpMethod.POST, "/api/operaciones-inventario/{idOperacion}/detalles")
                            .hasAnyRole("ALMACEN", "ADMIN") // Crear un nuevo detalle de operación de inventario
                        .requestMatchers(HttpMethod.POST, "/api/operaciones-inventario/registrar")
                            .hasAnyRole("ALMACEN", "ADMIN") // Registrar operación de inventario con detalles
                        .requestMatchers(HttpMethod.GET, "/api/operaciones-inventario")
                            .hasAnyRole("ALMACEN", "ADMIN") // Listar todas las operaciones de inventario
                        .requestMatchers(HttpMethod.GET, "/api/operaciones-inventario/operacion/{idOperacion}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Obtener detalles de operación por ID de operación
                        .requestMatchers(HttpMethod.GET, "/api/operaciones-inventario/detalles")
                            .hasAnyRole("ALMACEN", "ADMIN") // Listar todos los detalles de operación de inventario

                        // Movimientos de inventario
                        .requestMatchers(HttpMethod.GET, "/api/movimientos-inventario/{id}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Obtener un movimiento de inventario por ID
                        .requestMatchers(HttpMethod.PUT, "/api/movimientos-inventario/{id}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Actualizar un movimiento de inventario existente
                        .requestMatchers(HttpMethod.DELETE, "/api/movimientos-inventario/{id}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Eliminar un movimiento de inventario
                        .requestMatchers(HttpMethod.GET, "/api/movimientos-inventario")
                            .hasAnyRole("ALMACEN", "ADMIN") // Listar todos los movimientos de inventario
                        .requestMatchers(HttpMethod.POST, "/api/movimientos-inventario")
                            .hasAnyRole("ALMACEN", "ADMIN") // Crear un nuevo movimiento de inventario
                        .requestMatchers(HttpMethod.GET, "/api/movimientos-inventario/producto/{idProducto}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Listar movimientos de inventario por ID de producto
                        .requestMatchers(HttpMethod.GET, "/api/movimientos-inventario/operacion/{idOperacion}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Listar movimientos de inventario por ID de operación
                        .requestMatchers(HttpMethod.GET, "/api/movimientos-inventario/encargado/{idEmpleado}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Listar movimientos de inventario por ID de empleado encargado

                        // Inventario general
                        .requestMatchers(HttpMethod.GET, "/api/inventario")
                            .permitAll() // Público
                        .requestMatchers(HttpMethod.GET, "/api/inventario/producto/{idProducto}")
                            .permitAll() // Público
                        .requestMatchers(HttpMethod.GET, "/api/inventario/{idInventario}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Restringido
                        .requestMatchers(HttpMethod.PUT, "/api/inventario/{idInventario}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Restringido
                        .requestMatchers(HttpMethod.DELETE, "/api/inventario/{idInventario}")
                            .hasAnyRole("ALMACEN", "ADMIN") // Restringido
                        .requestMatchers(HttpMethod.POST, "/api/inventario")
                            .hasAnyRole("ALMACEN", "ADMIN") // Restringido

                        
                        // Clientes
                        .requestMatchers(HttpMethod.GET, "/api/clientes")
                            .hasAnyRole("VENTAS", "ADMIN") // Listar todos los clientes
                        .requestMatchers(HttpMethod.GET, "/api/clientes/{id}")
                            .hasAnyRole("CLIENTE", "VENTAS", "ADMIN") // Obtener un cliente por ID
                        .requestMatchers(HttpMethod.GET, "/api/clientes/email/{email}")
                            .hasAnyRole("CLIENTE", "VENTAS", "ADMIN") // Obtener un cliente por email
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/{id}")
                            .hasAnyRole("CLIENTE", "ADMIN") // Actualizar un cliente existente
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/{id}")
                            .hasRole("ADMIN") // Eliminar un cliente

                        // Compras online
                        .requestMatchers(HttpMethod.POST, "/api/compras/online")
                            .hasAnyRole("CLIENTE", "ADMIN") // Crear una compra online
                        
                        // Carritos
                        .requestMatchers(HttpMethod.GET, "/api/carritos")
                            .hasAnyRole("CLIENTE", "ADMIN") // Listar todos los carritos
                        .requestMatchers(HttpMethod.GET, "/api/carritos/cliente/{idCliente}")
                            .hasAnyRole("CLIENTE", "ADMIN") // Obtener carrito por cliente
                        .requestMatchers(HttpMethod.PUT, "/api/carritos/{idCarrito}")
                            .hasAnyRole("CLIENTE", "ADMIN") // Actualizar la fecha de modificación de un carrito
                        .requestMatchers(HttpMethod.DELETE, "/api/carritos/{idCarrito}")
                            .hasAnyRole("CLIENTE", "ADMIN") // Eliminar un carrito
                        .requestMatchers(HttpMethod.PUT, "/api/carritos/{idCarrito}/detalles/{idDetalle}")
                            .hasAnyRole("CLIENTE", "ADMIN") // Actualizar la cantidad de un producto en el carrito
                        .requestMatchers(HttpMethod.DELETE, "/api/carritos/{idCarrito}/detalles/{idDetalle}")
                            .hasAnyRole("CLIENTE", "ADMIN") // Eliminar un producto del carrito
                        .requestMatchers(HttpMethod.GET, "/api/carritos/{idCarrito}/detalles")
                            .hasAnyRole("CLIENTE", "ADMIN") // Listar detalles de un carrito
                        .requestMatchers(HttpMethod.POST, "/api/carritos/{idCarrito}/detalles")
                            .hasAnyRole("CLIENTE", "ADMIN") // Agregar un producto al carrito
                        .requestMatchers(HttpMethod.DELETE, "/api/carritos/{idCarrito}/detalles/eliminar")
                            .hasAnyRole("CLIENTE", "ADMIN")

                        
                        // Crear/editar productos
                        .requestMatchers(HttpMethod.POST, "/api/uploads/signed-url")
                            .hasAnyRole("ADMIN", "ALMACEN")
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
                        .requestMatchers(HttpMethod.GET, "/api/metodos-pago/{idMetodo}")
                            .permitAll() // Obtener un método de pago por ID
                        .requestMatchers(HttpMethod.PUT, "/api/metodos-pago/{idMetodo}")
                            .hasRole("ADMIN") // Actualizar un método de pago existente
                        .requestMatchers(HttpMethod.DELETE, "/api/metodos-pago/{idMetodo}")
                            .hasRole("ADMIN") // Eliminar un método de pago
                        .requestMatchers(HttpMethod.GET, "/api/metodos-pago")
                            .permitAll() // Listar todos los métodos de pago
                        .requestMatchers(HttpMethod.POST, "/api/metodos-pago")
                            .hasRole("ADMIN") // Crear un nuevo método de pago
                        .requestMatchers(HttpMethod.GET, "/api/metodos-pago/nombre/{nombre}")
                            .permitAll() // Obtener un método de pago por nombre
                        
                        // Empleados
                        .requestMatchers(HttpMethod.PUT, "/api/empleados/{id}")
                            .hasRole("ADMIN") // Actualizar un empleado existente
                        .requestMatchers(HttpMethod.DELETE, "/api/empleados/{id}")
                            .hasRole("ADMIN") // Eliminar un empleado
                        .requestMatchers(HttpMethod.GET, "/api/empleados")
                            .hasRole("ADMIN") // Listar todos los empleados
                        .requestMatchers(HttpMethod.POST, "/api/empleados")
                            .hasRole("ADMIN") // Registrar un nuevo empleado
                        .requestMatchers(HttpMethod.GET, "/api/empleados/usuario/{idUsuario}")
                            .hasAnyRole("VENTAS", "ADMIN", "ALMACEN", "PEDIDOS") // Obtener un empleado por ID de usuario
                        .requestMatchers(HttpMethod.GET, "/api/empleados/email/{email}")
                            .hasAnyRole("VENTAS", "ADMIN", "ALMACEN", "PEDIDOS") // Obtener un empleado por email de usuario


                        // Categorías
                        .requestMatchers(HttpMethod.GET, "/api/categorias/{id}")
                            .permitAll() // Obtener una categoría por ID
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/{id}")
                            .hasRole("ADMIN") // Actualizar una categoría existente
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/{id}")
                            .hasRole("ADMIN") // Eliminar una categoría
                        .requestMatchers(HttpMethod.GET, "/api/categorias")
                            .permitAll() // Listar todas las categorías (público)
                        .requestMatchers(HttpMethod.POST, "/api/categorias")
                            .hasRole("ADMIN") // Crear una nueva categoría
                        .requestMatchers(HttpMethod.GET, "/api/categorias/nombre/{nombre}")
                            .permitAll() // Obtener una categoría por nombre (público)

                        // Usuarios empleados
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-empleados/{id}")
                            .hasRole("ADMIN") // Obtener un usuario empleado por ID
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios-empleados/{id}")
                            .hasRole("ADMIN") // Actualizar un usuario empleado
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios-empleados/{id}")
                            .hasRole("ADMIN") // Eliminar un usuario empleado
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios-empleados/desactivar/{id}")
                            .hasRole("ADMIN") // Desactivar un usuario empleado
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-empleados")
                            .hasRole("ADMIN") // Listar todos los usuarios de los empleados

                        // Usuarios clientes
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios-clientes/desactivar/{id}")
                            .hasRole("ADMIN") // Desactivar un usuario cliente
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-clientes")
                            .hasRole("ADMIN") // Listar todos los usuarios de los clientes
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-clientes/{id}")
                            .hasAnyRole("ADMIN", "VENTAS") // Obtener un usuario cliente por ID
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios-clientes/{id}")
                            .hasRole("ADMIN") // Eliminar un usuario cliente
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-clientes/oauth")
                            .hasRole("ADMIN") // Obtener un usuario cliente por proveedor OAuth y ID OAuth
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-clientes/exists/email/{email}")
                            .hasRole("ADMIN") // Verificar existencia de usuario cliente por email
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-clientes/email/{email}")
                            .hasAnyRole("CLIENTE", "ADMIN", "VENTAS") // Obtener un usuario cliente por email



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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "https://signusfrontend-dnrv.vercel.app", "https://signusfrontend.vercel.app/"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
