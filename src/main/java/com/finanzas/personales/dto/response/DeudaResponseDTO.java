package com.finanzas.personales.dto.response;

import com.finanzas.personales.model.Deuda;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DeudaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;

    private BigDecimal montoTotal;
    private BigDecimal montoCuota;

    private int cuotasTotales;
    private int cuotasPagadas;
    private int cuotasRestantes;

    private BigDecimal montoPendiente;

    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;

    private Boolean saldada;

    private String cuentaNombre;

    public static DeudaResponseDTO from(Deuda d) {

        DeudaResponseDTO dto = new DeudaResponseDTO();

        dto.setId(d.getId());
        dto.setNombre(d.getNombre());
        dto.setDescripcion(d.getDescripcion());

        dto.setMontoTotal(d.getMontoTotal());
        dto.setMontoCuota(d.getMontoCuota());

        dto.setCuotasTotales(d.getCuotasTotales());
        dto.setCuotasPagadas(d.getCuotasPagadas());

        dto.setCuotasRestantes(
                d.getCuotasTotales() - d.getCuotasPagadas()
        );

        dto.setMontoPendiente(
                d.getMontoTotal().subtract(
                        d.getMontoCuota().multiply(
                                BigDecimal.valueOf(d.getCuotasPagadas())
                        )
                )
        );

        dto.setFechaInicio(d.getFechaInicio());

        dto.setFechaFinEstimada(
                d.getFechaInicio().plusMonths(
                        d.getCuotasTotales()
                )
        );

        dto.setSaldada(d.getSaldada());

        if (d.getCuenta() != null) {
            dto.setCuentaNombre(
                    d.getCuenta().getNombre()
            );
        }

        return dto;
    }
}
