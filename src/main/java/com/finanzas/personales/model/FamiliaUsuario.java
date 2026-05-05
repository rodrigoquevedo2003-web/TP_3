package com.finanzas.personales.model;

public class FamiliaUsuario {

    private Integer idFamiliaUsuario;
    private Integer idFamilia;
    private Integer idUsuario;
    private String rol;

    public FamiliaUsuario() {
    }

    public FamiliaUsuario(Integer idFamiliaUsuario, Integer idFamilia, Integer idUsuario, String rol) {
        this.idFamiliaUsuario = idFamiliaUsuario;
        this.idFamilia = idFamilia;
        this.idUsuario = idUsuario;
        this.rol = rol;
    }

    public Integer getIdFamiliaUsuario() {
        return idFamiliaUsuario;
    }

    public void setIdFamiliaUsuario(Integer idFamiliaUsuario) {
        this.idFamiliaUsuario = idFamiliaUsuario;
    }

    public Integer getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}