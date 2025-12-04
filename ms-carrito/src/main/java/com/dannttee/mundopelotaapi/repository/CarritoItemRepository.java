package com.dannttee.mundopelotaapi.repository;

import com.dannttee.mundopelotaapi.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    
    List<CarritoItem> findByUsuarioId(Long usuarioId);
    Optional<CarritoItem> findByUsuarioIdAndPelotaId(Long usuarioId, Long pelotaId);
    void deleteByUsuarioId(Long usuarioId);
}