package com.finanzas.personales.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import com.finanzas.personales.enums.TipoDeuda;
import com.finanzas.personales.enums.PeriodicidadInteres;
import java.time.LocalDate;

@Data
public class DeudaRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal montoTotal;

    @Min(1)
    private int cuotasTotales;

    @NotNull
    private LocalDate fechaInicio;

    private Long cuentaId;

    private Long categoriaId;

    private Boolean tasaUva = false;

    private BigDecimal montoEnUva;

    @NotNull
    private TipoDeuda tipoDeuda;
    
    private BigDecimal interestRate;
    private PeriodicidadInteres interestPeriod;
}
