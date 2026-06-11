package com.finanzas.personales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finanzas.personales.enums.TipoMovimiento;
import com.finanzas.personales.model.Deuda;
import com.finanzas.personales.model.TarjetaCredito;
import com.finanzas.personales.model.Statement;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="movimiento")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    @JsonIgnoreProperties({"movimientos", "usuario"})
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonIgnoreProperties({"movimientos", "usuario"})
    private Categoria categoria;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "deuda_id")
    @JsonIgnoreProperties({"usuario", "cuenta"})
    private Deuda deuda;

    @ManyToOne
    @JoinColumn(name = "tarjeta_id")
    @JsonIgnoreProperties({"usuario", "cuentaPago"})
    private TarjetaCredito tarjeta;

    @ManyToOne
    @JoinColumn(name = "statement_id")
    @JsonIgnoreProperties({"tarjeta"})
    private Statement statement;
}