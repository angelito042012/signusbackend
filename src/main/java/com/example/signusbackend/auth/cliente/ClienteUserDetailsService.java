package com.example.signusbackend.auth.cliente;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.UsuarioCliente;
import com.example.signusbackend.repository.UsuarioClienteRepository;

@Service
public class ClienteUserDetailsService implements UserDetailsService{

    private final UsuarioClienteRepository repo;

    public ClienteUserDetailsService(UsuarioClienteRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
                .map(ClienteUserDetails::new)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    /*public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsuarioCliente usuario = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return new ClienteUserDetails(usuario);
    }*/
    
    
}
