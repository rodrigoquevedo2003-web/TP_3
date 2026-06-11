package com.finanzas.personales.dto.response;

import com.finanzas.personales.model.Deuda;
import lombok.Data;

import java.math.BigDecimal;
import com.finanzas.personales.enums.TipoDeuda;
import com.finanzas.personales.enums.PeriodicidadInteres;
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

    private Boolean tasaUva;
    private BigDecimal montoEnUva;
    private BigDecimal uvaValorInicial;
    private TipoDeuda tipoDeuda;
    private java.math.BigDecimal montoActualPesos;
    private java.math.BigDecimal cuotaActualPesos;
    private java.math.BigDecimal pendienteActualPesos;
    private java.math.BigDecimal interestRate;
    private PeriodicidadInteres interestPeriod;
    private java.math.BigDecimal totalPagado;
    private java.util.List<com.finanzas.personales.dto.response.PagoDTO> pagos;

    public void setMontoActualPesos(java.math.BigDecimal montoActualPesos) {
        this.montoActualPesos = montoActualPesos;
    }

    public void setCuotaActualPesos(java.math.BigDecimal cuotaActualPesos) {
        this.cuotaActualPesos = cuotaActualPesos;
    }

    public void setPendienteActualPesos(java.math.BigDecimal pendienteActualPesos) {
        this.pendienteActualPesos = pendienteActualPesos;
    }

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
                d.getMontoCuota().multiply(
                        BigDecimal.valueOf(
                                d.getCuotasTotales() - d.getCuotasPagadas()
                        )
                )
        );

        dto.setFechaInicio(d.getFechaInicio());

        dto.setFechaFinEstimada(
                d.getFechaInicio().plusMonths(
                        d.getCuotasTotales() - 1L
                )
        );

        dto.setSaldada(d.getSaldada());

        if (d.getCuenta() != null) {
            dto.setCuentaNombre(
                    d.getCuenta().getNombre()
            );
        }

        dto.setTasaUva(d.getTasaUva());
        dto.setMontoEnUva(d.getMontoEnUva());
        dto.setUvaValorInicial(d.getUvaValorInicial());
        dto.setTipoDeuda(d.getTipoDeuda());
        dto.setInterestRate(d.getInterestRate());
        dto.setInterestPeriod(d.getInterestPeriod());
        dto.setMontoActualPesos(null);
        dto.setCuotaActualPesos(null);
        dto.setPendienteActualPesos(null);
        dto.setTotalPagado(null);
        dto.setPagos(null);

        return dto;
    }

    public void setPagos(java.util.List<com.finanzas.personales.dto.response.PagoDTO> pagos) {
        this.pagos = pagos;
    }

    public void setTotalPagado(java.math.BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }
}
