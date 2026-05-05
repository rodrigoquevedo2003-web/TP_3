package com.finanzas.personales.service;

import com.finanzas.personales.dao.FamiliaDAO;
import com.finanzas.personales.model.Familia;
import org.springframework.stereotype.Service;

@Service
public class FamiliaService {

    private final FamiliaDAO familiaDAO;

    public FamiliaService(FamiliaDAO familiaDAO) {
        this.familiaDAO = familiaDAO;
    }

    public void crearFamilia(Familia familia) {
        familiaDAO.crearFamilia(familia);
    }
}
