package com.finanzas.personales.controller;

import com.finanzas.personales.model.Familia;
import com.finanzas.personales.service.FamiliaService;
import org.springframework.web.bind.annotation.*;
import com.finanzas.personales.model.FamiliaUsuario;

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

    @PutMapping("/{idFamilia}/miembros/{idUsuario}/rol")
    public String cambiarRolMiembro(
            @PathVariable Integer idFamilia,
            @PathVariable Integer idUsuario,
            @RequestBody FamiliaUsuario familiaUsuario) {

        familiaService.cambiarRolMiembro(
                idFamilia,
                idUsuario,
                familiaUsuario.getRol()
        );

        return "Rol actualizado correctamente";
    }
}
