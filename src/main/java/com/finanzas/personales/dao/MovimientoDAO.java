package com.finanzas.personales.dao;

import com.finanzas.personales.model.Movimiento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MovimientoDAO {

    private final JdbcTemplate jdbcTemplate;

    public MovimientoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void guardar(Movimiento movimiento) {
        String sql = "INSERT INTO movimientos " +
                "(id_usuario, id_familia, id_categoria, tipo, descripcion, monto, fecha, es_familiar) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                movimiento.getIdUsuario(),
                movimiento.getIdFamilia(),
                movimiento.getIdCategoria(),
                movimiento.getTipo(),
                movimiento.getDescripcion(),
                movimiento.getMonto(),
                movimiento.getFecha(),
                movimiento.getEsFamiliar()
        );
    }

    public List<Movimiento> listarPorUsuario(Integer idUsuario) {
        String sql = "SELECT * FROM movimientos WHERE id_usuario = ? ORDER BY fecha DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Movimiento m = new Movimiento();
            m.setIdMovimiento(rs.getInt("id_movimiento"));
            m.setIdUsuario(rs.getInt("id_usuario"));
            m.setIdFamilia(rs.getObject("id_familia", Integer.class));
            m.setIdCategoria(rs.getInt("id_categoria"));
            m.setTipo(rs.getString("tipo"));
            m.setDescripcion(rs.getString("descripcion"));
            m.setMonto(rs.getBigDecimal("monto"));
            m.setFecha(rs.getDate("fecha").toLocalDate());
            m.setEsFamiliar(rs.getBoolean("es_familiar"));
            return m;
        }, idUsuario);
    }

    public List<Movimiento> listarPorFamilia(Integer idFamilia) {
        String sql = "SELECT * FROM movimientos WHERE id_familia = ? ORDER BY fecha DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Movimiento m = new Movimiento();
            m.setIdMovimiento(rs.getInt("id_movimiento"));
            m.setIdUsuario(rs.getInt("id_usuario"));
            m.setIdFamilia(rs.getObject("id_familia", Integer.class));
            m.setIdCategoria(rs.getInt("id_categoria"));
            m.setTipo(rs.getString("tipo"));
            m.setDescripcion(rs.getString("descripcion"));
            m.setMonto(rs.getBigDecimal("monto"));
            m.setFecha(rs.getDate("fecha").toLocalDate());
            m.setEsFamiliar(rs.getBoolean("es_familiar"));
            return m;
        }, idFamilia);
    }
}
