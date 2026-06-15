package com.finanzas.personales.dto.request;

import com.finanzas.personales.enums.TipoCuenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CuentaUpdateRequestDTO {

    @NotBlank(message = "El nombre de la cuenta es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipoCuenta;
}
