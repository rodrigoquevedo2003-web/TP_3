package com.finanzas.personales.dto;

import com.finanzas.personales.enums.TipoCuenta;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CuentaUpdateDTO {

    @NotBlank(message = "El nombre de la cuenta es obligatorio")
    private String nombre;

    private TipoCuenta tipoCuenta;
}
