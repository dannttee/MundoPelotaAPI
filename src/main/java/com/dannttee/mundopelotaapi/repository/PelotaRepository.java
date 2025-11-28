package com.dannttee.mundopelotaapi.repository;

import com.dannttee.mundopelotaapi.model.Pelota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PelotaRepository extends JpaRepository<Pelota, Long> {
    List<Pelota> findByDeporte(String deporte);
    List<Pelota> findByMarca(String marca);
    List<Pelota> findByStockGreaterThan(Integer stock);
}
