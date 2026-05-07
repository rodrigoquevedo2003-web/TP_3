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

    public Deuda buscarPorId(Integer idDeuda) {

        String sql = "SELECT * FROM deudas WHERE id_deuda = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

            Deuda d = new Deuda();

            d.setIdDeuda(rs.getInt("id_deuda"));
            d.setIdUsuario(rs.getInt("id_usuario"));
            d.setIdFamilia(rs.getObject("id_familia", Integer.class));

            d.setNombre(rs.getString("nombre"));
            d.setDescripcion(rs.getString("descripcion"));

            d.setMontoTotal(rs.getBigDecimal("monto_total"));
            d.setMontoPagado(rs.getBigDecimal("monto_pagado"));

            d.setCantidadCuotas(rs.getInt("cantidad_cuotas"));
            d.setCuotasPagadas(rs.getInt("cuotas_pagadas"));

            d.setMontoCuota(rs.getBigDecimal("monto_cuota"));

            d.setEstado(rs.getString("estado"));

            return d;

        }, idDeuda);
    }

    public void actualizarPago(Integer idDeuda,
                               Integer cuotasPagadas,
                               java.math.BigDecimal montoPagado,
                               String estado) {

        String sql = """
            UPDATE deudas
            SET cuotas_pagadas = ?,
                monto_pagado = ?,
                estado = ?
            WHERE id_deuda = ?
            """;

        jdbcTemplate.update(sql,
                cuotasPagadas,
                montoPagado,
                estado,
                idDeuda);
    }
}