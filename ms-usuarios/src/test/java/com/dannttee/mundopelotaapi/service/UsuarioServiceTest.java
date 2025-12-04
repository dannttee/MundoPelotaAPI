package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.Usuario;
import com.dannttee.mundopelotaapi.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void obtenerPorId_DeberiaRetornarUsuarioSiExiste() {
        // 1. Preparar
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setId(99L);
        usuarioSimulado.setNombre("Test User");

        Mockito.when(usuarioRepository.findById(99L)).thenReturn(Optional.of(usuarioSimulado));

        // 2. Actuar
        Optional<Usuario> resultado = usuarioService.obtenerPorId(99L);

        // 3. Verificar
        assertTrue(resultado.isPresent());
        assertEquals("Test User", resultado.get().getNombre());
    }
}