package com.finanzas.personales.dao;

import com.finanzas.personales.model.Transferencia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransferenciaDAO {

    private final JdbcTemplate jdbcTemplate;

    public TransferenciaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void guardar(Transferencia transferencia) {

        String sql = """
                INSERT INTO transferencias
                (id_usuario_origen, id_usuario_destino, id_familia, monto, descripcion, fecha)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                transferencia.getIdUsuarioOrigen(),
                transferencia.getIdUsuarioDestino(),
                transferencia.getIdFamilia(),
                transferencia.getMonto(),
                transferencia.getDescripcion(),
                transferencia.getFecha()
        );
    }
}