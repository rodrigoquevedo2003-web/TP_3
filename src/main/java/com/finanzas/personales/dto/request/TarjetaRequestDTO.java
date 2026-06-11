package com.finanzas.personales.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TarjetaRequestDTO {
    private String nombre;
    private Long cuentaPagoId;
    private BigDecimal limiteCredito;
    private Integer cierreDia;
    private Integer diasVencimiento;
    private BigDecimal tasaAnual;
}

