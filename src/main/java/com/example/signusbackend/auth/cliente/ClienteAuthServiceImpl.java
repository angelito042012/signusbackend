package com.example.signusbackend.auth.cliente;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.signusbackend.auth.cliente.dto.ClienteLoginRequest;
import com.example.signusbackend.auth.cliente.dto.ClienteRegisterRequest;
import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.entity.UsuarioCliente;
import com.example.signusbackend.repository.ClienteRepository;
import com.example.signusbackend.repository.UsuarioClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteAuthServiceImpl implements ClienteAuthService {
    
    private final UsuarioClienteRepository usuarioRepo;
    private final ClienteRepository clienteRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(ClienteLoginRequest request) {
        // Buscar el usuario por email
        UsuarioCliente usuario = usuarioRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar la contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Retornar un mensaje de éxito (luego puedes reemplazar esto con un JWT)
        return "Login exitoso (luego reemplazar con JWT)";
    }

    @Override
    public UsuarioCliente register(ClienteRegisterRequest req) {
        // Validar que el email no esté ya registrado
        if (usuarioRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado: " + req.getEmail());
        }

        // Crear un nuevo usuario cliente
        UsuarioCliente usuario = new UsuarioCliente();
        usuario.setEmail(req.getEmail());
        usuario.setContrasena(passwordEncoder.encode(req.getPassword())); // Encriptar la contraseña
        usuario.setEstado("Activo");

        // Guardar el usuario en la base de datos
        UsuarioCliente savedUser = usuarioRepo.save(usuario);

        // Crear el cliente asociado
        Cliente cliente = new Cliente();
        cliente.setUsuarioCliente(savedUser); // Relación con el usuario
        cliente.setNombres(req.getNombres());
        cliente.setApellidos(req.getApellidos());
        cliente.setDni(req.getDni());
        cliente.setDireccion(req.getDireccion());
        cliente.setTelefono(req.getTelefono());

        // Guardar el cliente en la base de datos
        clienteRepo.save(cliente);

        return savedUser;
    }
    
}
