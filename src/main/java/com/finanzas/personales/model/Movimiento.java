package com.finanzas.personales.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Movimiento {

    private Integer idMovimiento;
    private Integer idUsuario;
    private Integer idFamilia;
    private Integer idCategoria;

    private String tipo;
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fecha;
    private Boolean esFamiliar;

    public Movimiento() {
    }

    public Movimiento(Integer idMovimiento, Integer idUsuario, Integer idFamilia,
                      Integer idCategoria, String tipo, String descripcion,
                      BigDecimal monto, LocalDate fecha, Boolean esFamiliar) {
        this.idMovimiento = idMovimiento;
        this.idUsuario = idUsuario;
        this.idFamilia = idFamilia;
        this.idCategoria = idCategoria;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.esFamiliar = esFamiliar;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getEsFamiliar() {
        return esFamiliar;
    }

    public void setEsFamiliar(Boolean esFamiliar) {
        this.esFamiliar = esFamiliar;
    }
}