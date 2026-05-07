package com.finanzas.personales.dao;

import com.finanzas.personales.model.Familia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

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

    public List<String> obtenerMiembros(Integer idFamilia) {
        String sql = "SELECT u.nombre, u.apellido, fu.rol " +
                "FROM familia_usuario fu " +
                "JOIN usuarios u ON fu.id_usuario = u.id_usuario " +
                "WHERE fu.id_familia = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        rs.getString("nombre") + " " +
                                rs.getString("apellido") + " - " +
                                rs.getString("rol")
                , idFamilia);
    }

    public Integer buscarFamiliaDelUsuario(Integer idUsuario) {
        String sql = "SELECT id_familia FROM familia_usuario WHERE id_usuario = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, idUsuario);
        } catch (Exception e) {
            return null;
        }
    }

    public void cambiarRolMiembro(Integer idFamilia, Integer idUsuario, String nuevoRol) {
        String sql = "UPDATE familia_usuario SET rol = ? WHERE id_familia = ? AND id_usuario = ?";

        jdbcTemplate.update(sql, nuevoRol, idFamilia, idUsuario);
    }

    public String obtenerRolUsuario(Integer idFamilia, Integer idUsuario) {
        String sql = "SELECT rol FROM familia_usuario WHERE id_familia = ? AND id_usuario = ?";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, idFamilia, idUsuario);
        } catch (Exception e) {
            return null;
        }
    }
}