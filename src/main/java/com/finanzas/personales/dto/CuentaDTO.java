package com.finanzas.personales.dto;

import com.finanzas.personales.enums.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaDTO {

    @NotBlank(message = "El nombre de la cuenta es obligatorio")
    private String nombre;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipoCuenta;
}