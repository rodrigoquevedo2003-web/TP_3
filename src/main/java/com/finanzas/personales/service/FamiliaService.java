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
        Integer idFamiliaCreada = familiaDAO.crearFamilia(familia);

        familiaDAO.agregarUsuarioAFamilia(
                idFamiliaCreada,
                familia.getIdUsuarioCreador(),
                "ADMIN"
        );
    }

    public List<String> obtenerMiembros(Integer idFamilia) {
        return familiaDAO.obtenerMiembros(idFamilia);
    }
}