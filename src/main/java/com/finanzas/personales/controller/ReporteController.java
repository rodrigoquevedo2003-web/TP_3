package com.finanzas.personales.controller;

import com.finanzas.personales.dto.ReporteCategoriaDTO;
import com.finanzas.personales.service.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/categorias/usuario/{idUsuario}")
    public List<ReporteCategoriaDTO> gastosPorCategoria(@PathVariable Integer idUsuario) {
        return reporteService.gastosPorCategoria(idUsuario);
    }

    @GetMapping("/categorias/usuario/{idUsuario}/ultimos/{meses}")
    public List<ReporteCategoriaDTO> gastosPorCategoriaUltimosMeses(
            @PathVariable Integer idUsuario,
            @PathVariable Integer meses) {
        return reporteService.gastosPorCategoriaUltimosMeses(idUsuario, meses);
    }
}