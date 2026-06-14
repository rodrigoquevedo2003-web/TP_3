package com.finanzas.personales.dto.request;

import com.finanzas.personales.enums.TipoMovimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovimientoRequestDTO {

        @NotNull(message = "La cuenta es obligatoria")
        private Long cuentaId;

        @NotNull(message = "La categoria es obligatoria")
        private Long categoriaId;

        @NotNull(message = "El tipo de movimiento es obligatorio")
        private TipoMovimiento tipo;

        private String descripcion;

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        private BigDecimal monto;

        private LocalDate fecha;
}   