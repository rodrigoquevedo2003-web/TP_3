package com.finanzas.personales.controller;

import com.finanzas.personales.model.Categoria;
import com.finanzas.personales.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listarTodas() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/tipo/{tipo}")
    public List<Categoria> listarPorTipo(@PathVariable String tipo) {
        return categoriaService.listarPorTipo(tipo);
    }
}