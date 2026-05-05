package com.finanzas.personales.dao;

import com.finanzas.personales.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, apellido, username, email, clave) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPassword()
        );
    }
}