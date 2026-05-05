package com.finanzas.personales.dao;

import com.finanzas.personales.model.Familia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class FamiliaDAO {

    private final JdbcTemplate jdbcTemplate;

    public FamiliaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer crearFamilia(Familia familia) {
        String sql = "INSERT INTO familias (nombre, id_usuario_creador) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, familia.getNombre());
            ps.setInt(2, familia.getIdUsuarioCreador());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void agregarUsuarioAFamilia(Integer idFamilia, Integer idUsuario, String rol) {
        String sql = "INSERT INTO familia_usuario (id_familia, id_usuario, rol) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, idFamilia, idUsuario, rol);
    }
}