package com.finanzas.personales.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActualizarUsuarioDTO {
    @NotBlank
    private String nombre;
}