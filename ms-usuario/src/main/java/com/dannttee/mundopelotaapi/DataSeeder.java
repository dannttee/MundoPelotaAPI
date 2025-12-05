package com.dannttee.mundopelotaapi;

import com.dannttee.mundopelotaapi.model.Usuario;
import com.dannttee.mundopelotaapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            
            Usuario u1 = new Usuario();
            u1.setNombre("Alumno Plaza");
            u1.setEmail("alumno@duoc.cl");
            u1.setPassword("123456"); 
            u1.setRol(Usuario.Rol.USER);

            usuarioRepository.save(u1);

            System.out.println("✅ USUARIO DE PRUEBA CREADO");
        }
    }
}