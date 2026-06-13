package com.finanzas.personales.controller;

import com.finanzas.personales.dto.response.AnalisisResponseDTO;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.AnalisisFinancieroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analisis")
@RequiredArgsConstructor
public class AnalisisController {

    private final AnalisisFinancieroService analisisService;

    @GetMapping
    public AnalisisResponseDTO analizar(@AuthenticationPrincipal Usuario usuario) {
        String analisis = analisisService.analizarFinanzas(usuario.getId());
        return new AnalisisResponseDTO(analisis);
    }
}