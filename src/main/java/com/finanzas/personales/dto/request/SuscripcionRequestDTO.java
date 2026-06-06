package com.finanzas.personales.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SuscripcionRequestDTO {

    @NotBlank
    private String nombre;

    @DecimalMin(value = "0.01")
    private BigDecimal monto;

    @Min(1) @Max(31)
    private int diaCobro;
}
