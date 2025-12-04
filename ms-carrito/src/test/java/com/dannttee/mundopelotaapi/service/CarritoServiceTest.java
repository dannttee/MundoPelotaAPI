package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.CarritoItem;
import com.dannttee.mundopelotaapi.repository.CarritoItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoItemRepository carritoRepository; 

    @InjectMocks
    private CarritoService carritoService; 

    @Test
    void agregarAlCarrito_DeberiaGuardarItem() {
        // 1. Preparar
        Long usuarioId = 1L;
        Long pelotaId = 5L;
        Integer cantidad = 3;

        CarritoItem itemAGuardar = new CarritoItem();
        itemAGuardar.setUsuarioId(usuarioId); 
        itemAGuardar.setPelotaId(pelotaId);   
        itemAGuardar.setCantidad(cantidad);
        
        Mockito.when(carritoRepository.save(itemAGuardar)).thenReturn(itemAGuardar);

        // 2. Actuar
        CarritoItem resultado = carritoService.agregarAlCarrito(usuarioId, pelotaId, cantidad);

        // 3. Verificar
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(pelotaId, resultado.getPelotaId());
        assertEquals(cantidad, resultado.getCantidad());

        Mockito.verify(carritoRepository, Mockito.times(1)).save(itemAGuardar);
    }
}