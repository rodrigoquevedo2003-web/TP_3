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


    List<ReglaRecurrente> findByActivaTrueAndProximaEjecucionLessThanEqual(LocalDate fecha);


    @Query("SELECT r FROM ReglaRecurrente r WHERE r.cuenta.usuario.id = :usuarioId")
    List<ReglaRecurrente> findByUsuarioId(@Param("usuarioId") Long usuarioId);



}
