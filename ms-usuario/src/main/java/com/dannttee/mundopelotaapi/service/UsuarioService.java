package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.Usuario;
import com.dannttee.mundopelotaapi.repository.UsuarioRepository;
import lombok.NonNull; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(@NonNull Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario crear(@NonNull Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(@NonNull Long id, @NonNull Usuario usuario) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(usuario.getNombre());
            u.setEmail(usuario.getEmail());
            u.setRol(usuario.getRol());
            return usuarioRepository.save(u);
        }).orElse(null);
    }

    public boolean eliminar(@NonNull Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Usuario> obtenerPorEmail(@NonNull String email) {
        return usuarioRepository.findByEmail(email);
    }
}