package com.finanzas.personales.dao;

import com.finanzas.personales.model.Familia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FamiliaDAO {

    private final JdbcTemplate jdbcTemplate;

    public FamiliaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void crearFamilia(Familia familia) {
        String sql = "INSERT INTO familias (nombre, id_usuario_creador) VALUES (?, ?)";

        jdbcTemplate.update(sql,
                familia.getNombre(),
                familia.getIdUsuarioCreador()
        );
    }
}
