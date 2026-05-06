package com.finanzas.personales.dto;

import java.math.BigDecimal;

public class ReporteCategoriaDTO {

    private String categoria;
    private BigDecimal total;

    public ReporteCategoriaDTO() {
    }

    public ReporteCategoriaDTO(String categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}