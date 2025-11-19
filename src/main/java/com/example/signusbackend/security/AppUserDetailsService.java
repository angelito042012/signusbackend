package com.example.signusbackend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.entity.UsuarioCliente;
import com.example.signusbackend.entity.UsuarioEmpleado;
import com.example.signusbackend.repository.EmpleadoRepository;
import com.example.signusbackend.repository.UsuarioClienteRepository;
import com.example.signusbackend.repository.UsuarioEmpleadoRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AppUserDetailsService implements UserDetailsService{

    private final UsuarioEmpleadoRepository usuarioEmpleadoRepo;
    private final EmpleadoRepository empleadoRepo;
    private final UsuarioClienteRepository usuarioClienteRepo;

    public AppUserDetailsService(UsuarioEmpleadoRepository usuarioEmpleadoRepo,
                                 UsuarioClienteRepository usuarioClienteRepo,
                                 EmpleadoRepository empleadoRepo) {
        this.usuarioEmpleadoRepo = usuarioEmpleadoRepo;
        this.usuarioClienteRepo = usuarioClienteRepo;
        this.empleadoRepo = empleadoRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        HttpServletRequest req =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();

        boolean isAdminLogin = req.getRequestURI().contains("/login-admin");

        // ================================
        // 1️⃣ Si es login de ADMIN → solo buscar empleados
        // ================================
        if (isAdminLogin) {

            UsuarioEmpleado loginEmpleado =
                    usuarioEmpleadoRepo.findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado"));

            Empleado empleado =
                    empleadoRepo.findByUsuarioEmpleado_IdUsuario(loginEmpleado.getIdUsuario())
                            .orElseThrow(() -> new UsernameNotFoundException("Empleado sin datos extendidos"));

            return new AppUserDetails(
                    loginEmpleado.getEmail(),
                    loginEmpleado.getContrasena(),
                    empleado.getRol(),
                    loginEmpleado.getEstado().equalsIgnoreCase("ACTIVO")
            );
        }

        // ================================
        // 2️⃣ Login de cliente → SOLO buscar clientes
        // ================================
        UsuarioCliente cliente =
                usuarioClienteRepo.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return new AppUserDetails(
                cliente.getEmail(),
                cliente.getContrasena(),
                "CLIENTE",
                cliente.getEstado().equalsIgnoreCase("ACTIVO")
        );
        
        /*// 1) buscar credenciales de empleado por email
        var usuarioEmpleadoOpt = usuarioEmpleadoRepo.findByEmail(email);
        if (usuarioEmpleadoOpt.isPresent()) {
            UsuarioEmpleado uEmp = usuarioEmpleadoOpt.get();

            // buscar perfil Empleado relacionado para obtener rol
            var empleadoOpt = empleadoRepo.findByUsuarioEmpleado_IdUsuario(uEmp.getIdUsuario());
            String role = empleadoOpt.map(Empleado::getRol).orElse("ADMIN"); // o alguna política por defecto
            boolean active = "ACTIVO".equalsIgnoreCase(uEmp.getEstado());

            return new AppUserDetails(uEmp.getEmail(), uEmp.getContrasena(), role, active);
        }

        // 2) si no, buscar cliente
        var clienteOpt = usuarioClienteRepo.findByEmail(email);
        if (clienteOpt.isPresent()) {
            UsuarioCliente c = clienteOpt.get();
            return new AppUserDetails(c.getEmail(), c.getContrasena(), "CLIENTE",
                                      "ACTIVO".equalsIgnoreCase(c.getEstado()));
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + email);

        */
        /*// buscar empleado (prioridad)
        return empleadoRepo.findByEmail(email)
            .map(e -> new AppUserDetails(
                    e.getEmail(),
                    e.getContrasena(),
                    e.getRol() == null ? "ADMIN" : e.getRol(), // asegúrate getRol existe en entidad empleado
                    "ACTIVO".equalsIgnoreCase(e.getEstado())
            ))
            .or(() -> clienteRepo.findByEmail(email)
                .map(c -> new AppUserDetails(
                    c.getEmail(),
                    c.getContrasena(),
                    "CLIENTE",
                    "ACTIVO".equalsIgnoreCase(c.getEstado())
                )))
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));*/
    }
    
}
