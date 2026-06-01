package com.finanzas.personales.model;

import com.finanzas.personales.enums.TipoCuenta;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;

    @Column(nullable = false)
    private Boolean activa = true;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "cuenta")
    private List<Movimiento> movimientos;

    public Cuenta() {
    }

    public Cuenta(Long id, String nombre, BigDecimal saldo, TipoCuenta tipoCuenta,
                  Boolean activa, Usuario usuario, List<Movimiento> movimientos) {
        this.id = id;
        this.nombre = nombre;
        this.saldo = saldo;
        this.tipoCuenta = tipoCuenta;
        this.activa = activa;
        this.usuario = usuario;
        this.movimientos = movimientos;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Boolean getActiva() {
        return activa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
}