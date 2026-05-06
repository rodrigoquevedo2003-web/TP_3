package com.finanzas.personales.service;

import com.finanzas.personales.dao.CategoriaDAO;
import com.finanzas.personales.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public List<Categoria> listarTodas() {
        return categoriaDAO.listarTodas();
    }

    public List<Categoria> listarPorTipo(String tipo) {
        return categoriaDAO.listarPorTipo(tipo);
    }
}