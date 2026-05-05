package com.finanzas.personales.model;

public class Familia {

    private Integer idFamilia;
    private String nombre;
    private Integer idUsuarioCreador;

    public Familia() {
    }

    public Familia(Integer idFamilia, String nombre, Integer idUsuarioCreador) {
        this.idFamilia = idFamilia;
        this.nombre = nombre;
        this.idUsuarioCreador = idUsuarioCreador;
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

    public Integer getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(Integer idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }
}