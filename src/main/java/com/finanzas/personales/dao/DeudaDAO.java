package com.finanzas.personales.dao;

import com.finanzas.personales.model.Deuda;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeudaDAO {

    private final JdbcTemplate jdbcTemplate;

    public DeudaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void guardar(Deuda deuda) {

        String sql = """
                INSERT INTO deudas
                (
                    id_usuario,
                    id_familia,
                    nombre,
                    descripcion,
                    monto_total,
                    monto_pagado,
                    fecha_inicio,
                    fecha_vencimiento,
                    estado,
                    cantidad_cuotas,
                    cuotas_pagadas,
                    monto_cuota,
                    dia_cierre_tarjeta,
                    dia_vencimiento_tarjeta,
                    tipo_deuda
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                deuda.getIdUsuario(),
                deuda.getIdFamilia(),
                deuda.getNombre(),
                deuda.getDescripcion(),
                deuda.getMontoTotal(),
                deuda.getMontoPagado(),
                deuda.getFechaInicio(),
                deuda.getFechaVencimiento(),
                deuda.getEstado(),
                deuda.getCantidadCuotas(),
                deuda.getCuotasPagadas(),
                deuda.getMontoCuota(),
                deuda.getDiaCierreTarjeta(),
                deuda.getDiaVencimientoTarjeta(),
                deuda.getTipoDeuda()
        );
    }
}