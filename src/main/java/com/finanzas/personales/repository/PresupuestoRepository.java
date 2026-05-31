package com.finanzas.personales.repository;

import com.finanzas.personales.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
}
