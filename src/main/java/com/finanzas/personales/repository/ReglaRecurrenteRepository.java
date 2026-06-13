package com.finanzas.personales.repository;



import com.finanzas.personales.model.ReglaRecurrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ReglaRecurrenteRepository extends JpaRepository<ReglaRecurrente, Long> {





    @Query("SELECT r FROM ReglaRecurrente r WHERE r.cuenta.usuario.id = :usuarioId")
    List<ReglaRecurrente> findByUsuarioId(@Param("usuarioId") Long usuarioId);


    @Query("""
    SELECT r FROM ReglaRecurrente r
    JOIN FETCH r.cuenta c
    JOIN FETCH c.usuario
    JOIN FETCH r.categoria
    WHERE r.activa = true AND r.proximaEjecucion <= :fecha
    """)
    List<ReglaRecurrente> findByActivaTrueAndProximaEjecucionLessThanEqual(@Param("fecha") LocalDate fecha);


}
