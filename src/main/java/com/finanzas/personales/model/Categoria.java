package com.finanzas.personales.model;

public class Categoria {

    private Integer idCategoria;
    private String nombre;
    private String tipo;
    private Boolean fija;

    public Categoria() {
    }

    public Categoria(Integer idCategoria, String nombre, String tipo, Boolean fija) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fija = fija;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getFija() {
        return fija;
    }

    public void setFija(Boolean fija) {
        this.fija = fija;
    }
}