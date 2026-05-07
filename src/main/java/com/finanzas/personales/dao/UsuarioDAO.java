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

    public Usuario buscarPorEmail(String email) {

        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

                Usuario u = new Usuario();

                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("clave"));

                return u;

            }, email);

        } catch (Exception e) {
            return null;
        }
    }
    
}