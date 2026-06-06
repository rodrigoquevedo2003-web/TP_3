package com.finanzas.personales.repository;

import com.finanzas.personales.enums.TipoMovimiento;
import com.finanzas.personales.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    List<Categoria> findByTipo(TipoMovimiento tipo);

}