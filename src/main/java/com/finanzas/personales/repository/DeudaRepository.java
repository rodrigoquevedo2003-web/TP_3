package com.finanzas.personales.repository;

import com.finanzas.personales.model.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    //Todas las deudas de un usuario ordenadas por fecha de inicio
    List<Deuda> findByUsuarioIdOrderByFechaInicioAsc(Long usuarioId);

    // Deudas filtradas por estado (saldada o no) ordenadas por fecha de inicio
    List<Deuda> findByUsuarioIdAndSaldadaOrderByFechaInicioAsc(
            Long usuarioId,
            Boolean saldada
    );

    //Busca una deuda específica que pertenezca al usuario
    Optional<Deuda> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );
}
