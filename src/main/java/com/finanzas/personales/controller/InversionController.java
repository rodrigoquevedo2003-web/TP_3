package com.finanzas.personales.controller;

import com.finanzas.personales.model.Inversion;
import com.finanzas.personales.service.InversionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inversiones")
public class InversionController {

    private final InversionService inversionService;

    public InversionController(InversionService inversionService) {
        this.inversionService = inversionService;
    }

    @PostMapping
    public String crearInversion(@RequestBody Inversion inversion) {

        inversionService.crearInversion(inversion);

        return "Inversión creada correctamente";
    }
}