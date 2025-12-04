package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.repository.PelotaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PelotaServiceTest {

    @Mock
    private PelotaRepository pelotaRepository; 
    @InjectMocks
    private PelotaService pelotaService;

    @Test
    void crearPelota_DeberiaRetornarPelota() {
        // 1. Preparar Given
        Pelota pelotaParaGuardar = new Pelota();
        pelotaParaGuardar.setMarca("Adidas");
        pelotaParaGuardar.setNombre("Al Rihla");
        
        Mockito.when(pelotaRepository.save(pelotaParaGuardar)).thenReturn(pelotaParaGuardar);

        // 2. Actuar When
        Pelota resultado = pelotaService.crear(pelotaParaGuardar);

        // 3. Verificar (hen
        assertNotNull(resultado); 
        assertEquals("Adidas", resultado.getMarca()); 
    }

    @Test
    void obtenerPorId_SiExiste_DeberiaRetornarla() {
        // 1. Preparar
        Pelota pelotaSimulada = new Pelota();
        pelotaSimulada.setId(1L);
        pelotaSimulada.setNombre("Jabulani");

        Mockito.when(pelotaRepository.findById(1L)).thenReturn(Optional.of(pelotaSimulada));

        // 2. Actuar
        Optional<Pelota> resultado = pelotaService.obtenerPorId(1L);

        // 3. Verificar
        assertTrue(resultado.isPresent());
        assertEquals("Jabulani", resultado.get().getNombre());
    }
}