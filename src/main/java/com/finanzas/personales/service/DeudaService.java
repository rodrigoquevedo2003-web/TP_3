package com.finanzas.personales.service;

import com.finanzas.personales.dao.DeudaDAO;
import com.finanzas.personales.dao.FamiliaDAO;
import com.finanzas.personales.model.Deuda;
import org.springframework.stereotype.Service;
import com.finanzas.personales.dao.MovimientoDAO;
import com.finanzas.personales.model.Movimiento;

import java.time.LocalDate;


import java.math.BigDecimal;

@Service
public class DeudaService {

    private final DeudaDAO deudaDAO;
    private final FamiliaDAO familiaDAO;
    private final MovimientoDAO movimientoDAO;

    public DeudaService(DeudaDAO deudaDAO,
                        FamiliaDAO familiaDAO,
                        MovimientoDAO movimientoDAO) {

        this.deudaDAO = deudaDAO;
        this.familiaDAO = familiaDAO;
        this.movimientoDAO = movimientoDAO;
    }

    public void crearDeuda(Deuda deuda) {

        Integer idFamilia = familiaDAO.buscarFamiliaDelUsuario(deuda.getIdUsuario());
        deuda.setIdFamilia(idFamilia);

        if (deuda.getMontoPagado() == null) {
            deuda.setMontoPagado(BigDecimal.ZERO);
        }

        if (deuda.getCuotasPagadas() == null) {
            deuda.setCuotasPagadas(0);
        }

        if (deuda.getEstado() == null) {
            deuda.setEstado("PENDIENTE");
        }

        if (deuda.getTipoDeuda() == null) {
            deuda.setTipoDeuda("OTRO");
        }

        if (deuda.getCantidadCuotas() != null && deuda.getMontoCuota() == null) {
            deuda.setMontoCuota(
                    deuda.getMontoTotal()
                            .divide(BigDecimal.valueOf(deuda.getCantidadCuotas()))
            );
        }

        deudaDAO.guardar(deuda);
    }


    public void pagarCuota(Integer idDeuda) {

        Deuda deuda = deudaDAO.buscarPorId(idDeuda);

        Integer nuevasCuotas = deuda.getCuotasPagadas() + 1;

        java.math.BigDecimal nuevoMontoPagado =
                deuda.getMontoPagado().add(deuda.getMontoCuota());

        String estado = "PARCIAL";

        if (nuevasCuotas >= deuda.getCantidadCuotas()) {
            estado = "PAGADA";
        }

        deudaDAO.actualizarPago(
                idDeuda,
                nuevasCuotas,
                nuevoMontoPagado,
                estado
        );


        Movimiento movimiento = new Movimiento();

        movimiento.setIdUsuario(deuda.getIdUsuario());
        movimiento.setIdFamilia(deuda.getIdFamilia());

        movimiento.setIdCategoria(1);

        movimiento.setTipo("GASTO");

        movimiento.setDescripcion(
                "Pago cuota deuda: " + deuda.getNombre()
        );

        movimiento.setMonto(deuda.getMontoCuota());

        movimiento.setFecha(LocalDate.now());

        movimiento.setEsFamiliar(true);

        movimientoDAO.guardar(movimiento);
    }
}