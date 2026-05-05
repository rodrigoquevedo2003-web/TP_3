package com.finanzas.personales.dao;

import com.finanzas.personales.model.Movimiento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
