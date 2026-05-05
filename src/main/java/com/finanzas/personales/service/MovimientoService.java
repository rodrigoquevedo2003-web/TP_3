package com.finanzas.personales.service;

import com.finanzas.personales.dao.FamiliaDAO;
import com.finanzas.personales.dao.MovimientoDAO;
import com.finanzas.personales.model.Movimiento;
import org.springframework.stereotype.Service;

@Service
public class MovimientoService {

    private final MovimientoDAO movimientoDAO;
    private final FamiliaDAO familiaDAO;

    public MovimientoService(MovimientoDAO movimientoDAO, FamiliaDAO familiaDAO) {
        this.movimientoDAO = movimientoDAO;
        this.familiaDAO = familiaDAO;
    }

    public void crearMovimiento(Movimiento movimiento) {

        // Buscar si el usuario tiene familia
        Integer idFamilia = familiaDAO.buscarFamiliaDelUsuario(movimiento.getIdUsuario());

        if (idFamilia != null) {
            movimiento.setIdFamilia(idFamilia);
            movimiento.setEsFamiliar(true);
        } else {
            movimiento.setEsFamiliar(false);
        }

        movimientoDAO.guardar(movimiento);
    }
}
