package com.dannttee.mundopelotaapi.repository;

import com.dannttee.mundopelotaapi.model.OrdenDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenDetalleRepository extends JpaRepository<OrdenDetalle, Long> {
}
