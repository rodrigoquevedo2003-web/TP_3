package com.finanzas.personales.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PagoDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDate fecha;
    private String descripcion;
    private String cuentaNombre;
    private String categoriaNombre;
}

