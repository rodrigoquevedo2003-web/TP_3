package com.finanzas.personales.controller;

import com.finanzas.personales.model.Transferencia;
import com.finanzas.personales.service.TransferenciaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    public String realizarTransferencia(@RequestBody Transferencia transferencia) {

        transferenciaService.realizarTransferencia(transferencia);

        return "Transferencia realizada correctamente";
    }
}