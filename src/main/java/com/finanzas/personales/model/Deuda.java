package com.finanzas.personales.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Deuda {

    private Integer idDeuda;
    private Integer idUsuario;
    private Integer idFamilia;

    private String nombre;
    private String descripcion;

    private BigDecimal montoTotal;
    private BigDecimal montoPagado;

    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;

    private String estado;

    private Integer cantidadCuotas;
    private Integer cuotasPagadas;
    private BigDecimal montoCuota;

    private Integer diaCierreTarjeta;
    private Integer diaVencimientoTarjeta;

    private String tipoDeuda;

    public Deuda() {
    }

    public Integer getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(Integer idDeuda) {
        this.idDeuda = idDeuda;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(Integer cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public Integer getCuotasPagadas() {
        return cuotasPagadas;
    }

    public void setCuotasPagadas(Integer cuotasPagadas) {
        this.cuotasPagadas = cuotasPagadas;
    }

    public BigDecimal getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(BigDecimal montoCuota) {
        this.montoCuota = montoCuota;
    }

    public Integer getDiaCierreTarjeta() {
        return diaCierreTarjeta;
    }

    public void setDiaCierreTarjeta(Integer diaCierreTarjeta) {
        this.diaCierreTarjeta = diaCierreTarjeta;
    }

    public Integer getDiaVencimientoTarjeta() {
        return diaVencimientoTarjeta;
    }

    public void setDiaVencimientoTarjeta(Integer diaVencimientoTarjeta) {
        this.diaVencimientoTarjeta = diaVencimientoTarjeta;
    }

    public String getTipoDeuda() {
        return tipoDeuda;
    }

    public void setTipoDeuda(String tipoDeuda) {
        this.tipoDeuda = tipoDeuda;
    }
}
