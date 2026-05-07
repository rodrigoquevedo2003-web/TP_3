package com.finanzas.personales.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transferencia {

    private Integer idTransferencia;
    private Integer idUsuarioOrigen;
    private Integer idUsuarioDestino;
    private Integer idFamilia;
    private BigDecimal monto;
    private String descripcion;
    private LocalDate fecha;

    public Transferencia() {
    }

    public Transferencia(Integer idTransferencia, Integer idUsuarioOrigen, Integer idUsuarioDestino,
                         Integer idFamilia, BigDecimal monto, String descripcion, LocalDate fecha) {
        this.idTransferencia = idTransferencia;
        this.idUsuarioOrigen = idUsuarioOrigen;
        this.idUsuarioDestino = idUsuarioDestino;
        this.idFamilia = idFamilia;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Integer getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(Integer idTransferencia) {
        this.idTransferencia = idTransferencia;
    }

    public Integer getIdUsuarioOrigen() {
        return idUsuarioOrigen;
    }

    public void setIdUsuarioOrigen(Integer idUsuarioOrigen) {
        this.idUsuarioOrigen = idUsuarioOrigen;
    }

    public Integer getIdUsuarioDestino() {
        return idUsuarioDestino;
    }

    public void setIdUsuarioDestino(Integer idUsuarioDestino) {
        this.idUsuarioDestino = idUsuarioDestino;
    }

    public Integer getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}