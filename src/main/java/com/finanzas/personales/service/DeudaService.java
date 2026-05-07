package com.finanzas.personales.service;

import com.finanzas.personales.dao.DeudaDAO;
import com.finanzas.personales.dao.FamiliaDAO;
import com.finanzas.personales.model.Deuda;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DeudaService {

    private final DeudaDAO deudaDAO;
    private final FamiliaDAO familiaDAO;

    public DeudaService(DeudaDAO deudaDAO, FamiliaDAO familiaDAO) {
        this.deudaDAO = deudaDAO;
        this.familiaDAO = familiaDAO;
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
}