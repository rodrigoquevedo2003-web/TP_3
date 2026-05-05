package com.finanzas.personales.model;

public class Invitacion {

    private Integer idInvitacion;
    private Integer idFamilia;
    private Integer idUsuarioInvita;
    private Integer idUsuarioInvitado;
    private String emailInvitado;
    private String rolInvitado;
    private String estado;

    public Invitacion() {
    }

    public Invitacion(Integer idInvitacion, Integer idFamilia, Integer idUsuarioInvita,
                      Integer idUsuarioInvitado, String emailInvitado,
                      String rolInvitado, String estado) {
        this.idInvitacion = idInvitacion;
        this.idFamilia = idFamilia;
        this.idUsuarioInvita = idUsuarioInvita;
        this.idUsuarioInvitado = idUsuarioInvitado;
        this.emailInvitado = emailInvitado;
        this.rolInvitado = rolInvitado;
        this.estado = estado;
    }

    public Integer getIdInvitacion() {
        return idInvitacion;
    }

    public void setIdInvitacion(Integer idInvitacion) {
        this.idInvitacion = idInvitacion;
    }

    public Integer getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }

    public Integer getIdUsuarioInvita() {
        return idUsuarioInvita;
    }

    public void setIdUsuarioInvita(Integer idUsuarioInvita) {
        this.idUsuarioInvita = idUsuarioInvita;
    }

    public Integer getIdUsuarioInvitado() {
        return idUsuarioInvitado;
    }

    public void setIdUsuarioInvitado(Integer idUsuarioInvitado) {
        this.idUsuarioInvitado = idUsuarioInvitado;
    }

    public String getEmailInvitado() {
        return emailInvitado;
    }

    public void setEmailInvitado(String emailInvitado) {
        this.emailInvitado = emailInvitado;
    }

    public String getRolInvitado() {
        return rolInvitado;
    }

    public void setRolInvitado(String rolInvitado) {
        this.rolInvitado = rolInvitado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
