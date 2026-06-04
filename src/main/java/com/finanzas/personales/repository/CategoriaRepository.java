package com.finanzas.personales.repository;

import com.finanzas.personales.enums.TipoMovimiento;
import com.finanzas.personales.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByTipo(TipoMovimiento tipo);

    boolean existsByNombreIgnoreCase(String nombre);
}