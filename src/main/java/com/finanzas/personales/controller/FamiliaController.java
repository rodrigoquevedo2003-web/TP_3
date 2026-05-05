package com.finanzas.personales.controller;

import com.finanzas.personales.model.Familia;
import com.finanzas.personales.service.FamiliaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/familias")
public class FamiliaController {

    private final FamiliaService familiaService;

    public FamiliaController(FamiliaService familiaService) {
        this.familiaService = familiaService;
    }

    @PostMapping
    public String crearFamilia(@RequestBody Familia familia) {
        familiaService.crearFamilia(familia);
        return "Familia creada correctamente";
    }

    @GetMapping("/{idFamilia}/miembros")
    public List<String> obtenerMiembros(@PathVariable Integer idFamilia) {
        return familiaService.obtenerMiembros(idFamilia);
    }
}
