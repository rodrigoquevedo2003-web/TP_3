package com.finanzas.personales.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Entity
@Table(name = "presupuesto")
public class Presupuesto {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal montoLimite;

    @Column(precision = 10, scale = 2)
    private BigDecimal montoConsumido;

    @Basic
    private int mes;

    @Basic
    private int anio;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Presupuesto(Long id, BigDecimal montoLimite, BigDecimal montoConsumido, int mes, int anio, Usuario usuario, Categoria categoria) {
        this.id = id;
        this.montoLimite = montoLimite;
        this.montoConsumido = montoConsumido;
        this.mes = mes;
        this.anio = anio;
        this.usuario = usuario;
        this.categoria = categoria;
    }

    public Presupuesto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontoLimite() {
        return montoLimite;
    }

    public void setMontoLimite(BigDecimal montoLimite) {
        this.montoLimite = montoLimite;
    }

    public BigDecimal getMontoConsumido() {
        return montoConsumido;
    }

    public void setMontoConsumido(BigDecimal montoConsumido) {
        this.montoConsumido = montoConsumido;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
