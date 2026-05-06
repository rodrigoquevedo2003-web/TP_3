package com.finanzas.personales.dao;

import com.finanzas.personales.dto.ReporteCategoriaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReporteDAO {

    private final JdbcTemplate jdbcTemplate;

    public ReporteDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReporteCategoriaDTO> gastosPorCategoria(Integer idUsuario) {

        String sql = """
                SELECT c.nombre AS categoria,
                       SUM(m.monto) AS total
                FROM movimientos m
                JOIN categorias c
                    ON m.id_categoria = c.id_categoria
                WHERE m.id_usuario = ?
                  AND m.tipo = 'GASTO'
                GROUP BY c.nombre
                ORDER BY total DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new ReporteCategoriaDTO(
                                rs.getString("categoria"),
                                rs.getBigDecimal("total")
                        )
                , idUsuario);
    }
}