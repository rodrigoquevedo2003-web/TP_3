package com.finanzas.personales.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statement")
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tarjeta_id", nullable = false)
    private TarjetaCredito tarjeta;

    private LocalDate periodoInicio;
    private LocalDate periodoFin;
    private LocalDate fechaVencimiento;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Column(precision = 10, scale = 2)
    private BigDecimal minimo;

    @Column(precision = 10, scale = 2)
    private BigDecimal pagado = BigDecimal.ZERO;

    private String estado; // ABIERTO, VENCIDO, PAGADO
}

