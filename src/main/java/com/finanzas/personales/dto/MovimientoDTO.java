package com.finanzas.personales.dto;

import com.finanzas.personales.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoDTO {

    private Long cuentaId;
    private Long categoriaId;
    private TipoMovimiento tipo;
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fecha;
    private Boolean esFamiliar;

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
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