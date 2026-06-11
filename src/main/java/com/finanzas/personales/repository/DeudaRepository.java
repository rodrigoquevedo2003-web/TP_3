package com.finanzas.personales.repository;

import com.finanzas.personales.model.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    List<Deuda> findByUsuarioIdOrderByFechaInicioAsc(Long usuarioId);

    List<Deuda> findByUsuarioIdAndSaldadaOrderByFechaInicioAsc(
            Long usuarioId,
            Boolean saldada
    );

    Optional<Deuda> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );
}
