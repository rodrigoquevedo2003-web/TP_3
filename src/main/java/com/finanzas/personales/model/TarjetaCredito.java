package com.finanzas.personales.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tarjeta_credito")
public class TarjetaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cuenta_pago_id")
    private Cuenta cuentaPago;

    @DecimalMin("0.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal limiteCredito;

    private Integer cierreDia;

    private Integer diasVencimiento;

    @Column(precision = 6, scale = 4)
    private BigDecimal tasaAnual;

    private Boolean activo = true;
}

