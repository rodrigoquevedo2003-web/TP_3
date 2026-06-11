package com.finanzas.personales.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import com.finanzas.personales.enums.TipoDeuda;
import com.finanzas.personales.enums.PeriodicidadInteres;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deuda")
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @NotNull
    @DecimalMin(value = "0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoCuota;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private int cuotasTotales;

    @Column(nullable = false)
    private int cuotasPagadas = 0;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private Boolean saldada = false;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_deuda")
    private TipoDeuda tipoDeuda;

    @Column(nullable = false)
    private Boolean tasaUva = false;

    @Column(precision = 10, scale = 4)
    private BigDecimal montoEnUva;

    @Column(precision = 10, scale = 4)
    private BigDecimal uvaValorInicial;

    @Column(precision = 6, scale = 4)
    private BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_period")
    private PeriodicidadInteres interestPeriod;
}
