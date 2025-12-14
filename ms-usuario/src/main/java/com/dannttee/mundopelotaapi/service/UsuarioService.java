package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.Usuario;
import com.dannttee.mundopelotaapi.repository.UsuarioRepository;
import lombok.NonNull; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate; 
import org.springframework.web.client.HttpClientErrorException; 
import org.springframework.web.client.ResourceAccessException; 

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${api.carrito.url}")
    private String carritoBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private void vaciarCarritoRemoto(Long usuarioId) {
        final String vaciarUrl = carritoBaseUrl + "/api/carritos/vaciar/" + usuarioId;
        
        try {
            restTemplate.delete(vaciarUrl);
            System.out.println("Carrito del usuario " + usuarioId + " vaciado exitosamente.");
        } catch (HttpClientErrorException ex) {
            System.err.println("Error al vaciar carrito: " + ex.getStatusCode());
        } catch (ResourceAccessException ex) {
            System.err.println("Servicio de carrito no accesible: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Error desconocido: " + ex.getMessage());
        }
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(@NonNull Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario crear(@NonNull Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(@NonNull Long id, @NonNull Usuario usuario) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(usuario.getNombre());
            u.setEmail(usuario.getEmail());
            u.setRol(usuario.getRol());
            return usuarioRepository.save(u);
        }).orElse(null);
    }

    @Transactional
    public boolean eliminar(@NonNull Long id) {
        if (usuarioRepository.existsById(id)) {
            vaciarCarritoRemoto(id);
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Usuario> obtenerPorEmail(@NonNull String email) {
        return usuarioRepository.findByEmail(email);
    }
}
