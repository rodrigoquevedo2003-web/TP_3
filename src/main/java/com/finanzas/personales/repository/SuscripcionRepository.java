package com.finanzas.personales.repository;

import com.finanzas.personales.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    List<Suscripcion> findByUsuarioId(Long usuarioId);

    List<Suscripcion> findByUsuarioIdAndActiva(Long usuarioId, Boolean activa);

    Optional<Suscripcion> findByIdAndUsuarioId(Long id, Long usuarioId);
}