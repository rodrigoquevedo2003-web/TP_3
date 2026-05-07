package com.finanzas.personales.service;

import com.finanzas.personales.dao.FamiliaDAO;
import com.finanzas.personales.dao.InversionDAO;
import com.finanzas.personales.dao.MovimientoDAO;
import com.finanzas.personales.model.Inversion;
import com.finanzas.personales.model.Movimiento;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InversionService {

    private final InversionDAO inversionDAO;
    private final FamiliaDAO familiaDAO;
    private final MovimientoDAO movimientoDAO;

    public InversionService(InversionDAO inversionDAO,
                            FamiliaDAO familiaDAO,
                            MovimientoDAO movimientoDAO) {

        this.inversionDAO = inversionDAO;
        this.familiaDAO = familiaDAO;
        this.movimientoDAO = movimientoDAO;
    }

    public void crearInversion(Inversion inversion) {

        Integer idFamilia = familiaDAO.buscarFamiliaDelUsuario(
                inversion.getIdUsuario()
        );

        inversion.setIdFamilia(idFamilia);

        inversionDAO.guardar(inversion);

        // Registrar movimiento

        Movimiento movimiento = new Movimiento();

        movimiento.setIdUsuario(inversion.getIdUsuario());

        movimiento.setIdFamilia(idFamilia);

        movimiento.setIdCategoria(1);

        movimiento.setTipo("INVERSION");

        movimiento.setDescripcion(
                "Inversión: " + inversion.getNombre()
        );

        movimiento.setMonto(inversion.getValorPesos());

        movimiento.setFecha(LocalDate.now());

        movimiento.setEsFamiliar(true);

        movimientoDAO.guardar(movimiento);
    }
}