package com.finanzas.personales.service;

import com.finanzas.personales.dao.FamiliaDAO;
import com.finanzas.personales.dao.InvitacionDAO;
import com.finanzas.personales.model.Invitacion;
import org.springframework.stereotype.Service;

@Service
public class InvitacionService {

    private final InvitacionDAO invitacionDAO;
    private final FamiliaDAO familiaDAO;

    public InvitacionService(InvitacionDAO invitacionDAO, FamiliaDAO familiaDAO) {
        this.invitacionDAO = invitacionDAO;
        this.familiaDAO = familiaDAO;
    }

    public void crearInvitacion(Invitacion invitacion) {
        invitacionDAO.crearInvitacion(invitacion);
    }

    public void aceptarInvitacion(Integer idInvitacion) {

        Invitacion inv = invitacionDAO.obtenerPorId(idInvitacion);

        // 1. Cambiar estado
        invitacionDAO.aceptarInvitacion(idInvitacion);

        // 2. Agregar a la familia
        familiaDAO.agregarUsuarioAFamilia(
                inv.getIdFamilia(),
                inv.getIdUsuarioInvitado(),
                inv.getRolInvitado()
        );
    }

    public void rechazarInvitacion(Integer idInvitacion) {
        invitacionDAO.rechazarInvitacion(idInvitacion);
    }
}