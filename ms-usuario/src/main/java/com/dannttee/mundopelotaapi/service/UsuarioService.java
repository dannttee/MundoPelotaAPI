package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.Usuario;
import com.dannttee.mundopelotaapi.repository.UsuarioRepository;
import lombok.NonNull; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service;
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

    /**
     
       @param usuarioId ID del usuario cuyo carrito debe vaciarse.
     */
    private void vaciarCarritoRemoto(Long usuarioId) {

        final String vaciarUrl = carritoBaseUrl + "/api/carritos/vaciar/" + usuarioId;
        
        try {
            restTemplate.delete(vaciarUrl);
            System.out.println("Carrito del usuario " + usuarioId + " vaciado exitosamente en el microservicio Carrito.");
            
        } catch (HttpClientErrorException ex) {

            System.err.println("Error al intentar vaciar el carrito del usuario " + usuarioId + ". Código: " + ex.getStatusCode());
        } catch (ResourceAccessException ex) {

            System.err.println("Advertencia: El servicio de Carrito no está accesible. No se pudo limpiar el carrito. " + ex.getMessage());
        } catch (Exception ex) {

            System.err.println("Error desconocido al vaciar carrito: " + ex.getMessage());
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

    public Usuario actualizar(@NonNull Long id, @NonNull Usuario usuario) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(usuario.getNombre());
            u.setEmail(usuario.getEmail());
            u.setRol(usuario.getRol());
            return usuarioRepository.save(u);
        }).orElse(null);
    }

    /**

       @param id ID del usuario a eliminar.
       @return true si se eliminó, false si no se encontró.
     */
    public boolean eliminar(@NonNull Long id) {
        if (usuarioRepository.existsById(id)) {
            // 1. Lógica de Integración: Primero limpiamos los datos asociados en el microservicio de Carrito
            vaciarCarritoRemoto(id); 

            // 2. Luego eliminamos el usuario de la DB local
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Usuario> obtenerPorEmail(@NonNull String email) {
        return usuarioRepository.findByEmail(email);
    }
}