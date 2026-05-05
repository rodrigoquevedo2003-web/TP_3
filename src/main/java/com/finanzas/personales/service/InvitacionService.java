package com.finanzas.personales.service;

import com.finanzas.personales.dao.InvitacionDAO;
import com.finanzas.personales.model.Invitacion;
import org.springframework.stereotype.Service;

@Service
public class InvitacionService {

    private final InvitacionDAO invitacionDAO;

    public InvitacionService(InvitacionDAO invitacionDAO) {
        this.invitacionDAO = invitacionDAO;
    }

    public void crearInvitacion(Invitacion invitacion) {
        invitacionDAO.crearInvitacion(invitacion);
    }

    public void aceptarInvitacion(Integer idInvitacion) {
        invitacionDAO.aceptarInvitacion(idInvitacion);
    }

    public void rechazarInvitacion(Integer idInvitacion) {
        invitacionDAO.rechazarInvitacion(idInvitacion);
    }
}
