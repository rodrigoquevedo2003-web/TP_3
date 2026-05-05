package com.finanzas.personales.controller;

import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.service.MovimientoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public String crearMovimiento(@RequestBody Movimiento movimiento) {
        movimientoService.crearMovimiento(movimiento);
        return "Movimiento creado correctamente";
    }
}