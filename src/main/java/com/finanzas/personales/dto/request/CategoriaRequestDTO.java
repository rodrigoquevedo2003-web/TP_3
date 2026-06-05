package com.finanzas.personales.dto.request;

import com.finanzas.personales.enums.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    private String nombre;

    private String icono;

    @NotNull(message = "El tipo de categoria es obligatorio (ingreso o egreso)")
    private TipoMovimiento tipo;
}
