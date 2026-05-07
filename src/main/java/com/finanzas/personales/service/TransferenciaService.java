package com.finanzas.personales.service;

import com.finanzas.personales.dao.FamiliaDAO;
import com.finanzas.personales.dao.MovimientoDAO;
import com.finanzas.personales.dao.TransferenciaDAO;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.model.Transferencia;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {

    private final TransferenciaDAO transferenciaDAO;
    private final MovimientoDAO movimientoDAO;
    private final FamiliaDAO familiaDAO;

    public TransferenciaService(TransferenciaDAO transferenciaDAO,
                                MovimientoDAO movimientoDAO,
                                FamiliaDAO familiaDAO) {
        this.transferenciaDAO = transferenciaDAO;
        this.movimientoDAO = movimientoDAO;
        this.familiaDAO = familiaDAO;
    }

    public void realizarTransferencia(Transferencia transferencia) {

        Integer idFamilia = familiaDAO.buscarFamiliaDelUsuario(
                transferencia.getIdUsuarioOrigen()
        );

        transferencia.setIdFamilia(idFamilia);

        transferenciaDAO.guardar(transferencia);

        Movimiento salida = new Movimiento();

        salida.setIdUsuario(transferencia.getIdUsuarioOrigen());
        salida.setIdFamilia(idFamilia);
        salida.setIdCategoria(1);
        salida.setTipo("TRANSFERENCIA");
        salida.setDescripcion("Transferencia enviada");
        salida.setMonto(transferencia.getMonto());
        salida.setFecha(transferencia.getFecha());
        salida.setEsFamiliar(true);

        movimientoDAO.guardar(salida);

        Movimiento entrada = new Movimiento();

        entrada.setIdUsuario(transferencia.getIdUsuarioDestino());
        entrada.setIdFamilia(idFamilia);
        entrada.setIdCategoria(1);
        entrada.setTipo("INGRESO");
        entrada.setDescripcion("Transferencia recibida");
        entrada.setMonto(transferencia.getMonto());
        entrada.setFecha(transferencia.getFecha());
        entrada.setEsFamiliar(true);

        movimientoDAO.guardar(entrada);
    }
}