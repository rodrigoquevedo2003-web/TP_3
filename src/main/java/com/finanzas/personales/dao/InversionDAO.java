package com.finanzas.personales.dao;

import com.finanzas.personales.model.Inversion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InversionDAO {

    private final JdbcTemplate jdbcTemplate;

    public InversionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void guardar(Inversion inversion) {

        String sql = """
                INSERT INTO inversiones
                (
                    id_usuario,
                    id_familia,
                    tipo,
                    nombre,
                    cantidad,
                    valor_pesos,
                    descripcion
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                inversion.getIdUsuario(),
                inversion.getIdFamilia(),
                inversion.getTipo(),
                inversion.getNombre(),
                inversion.getCantidad(),
                inversion.getValorPesos(),
                inversion.getDescripcion()
        );
    }
}
