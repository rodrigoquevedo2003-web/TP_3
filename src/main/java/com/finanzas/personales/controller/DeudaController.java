package com.finanzas.personales.controller;

import com.finanzas.personales.model.Deuda;
import com.finanzas.personales.service.DeudaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deudas")
public class DeudaController {

    private final DeudaService deudaService;

    public DeudaController(DeudaService deudaService) {
        this.deudaService = deudaService;
    }

    @PostMapping
    public String crearDeuda(@RequestBody Deuda deuda) {
        deudaService.crearDeuda(deuda);
        return "Deuda creada correctamente";
    }

    @PutMapping("/{idDeuda}/pagar-cuota")
    public String pagarCuota(@PathVariable Integer idDeuda) {

        deudaService.pagarCuota(idDeuda);

        return "Cuota pagada correctamente";
    }
}