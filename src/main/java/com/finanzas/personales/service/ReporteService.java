package com.finanzas.personales.service;

import com.finanzas.personales.dao.ReporteDAO;
import com.finanzas.personales.dto.ReporteCategoriaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {

    private final ReporteDAO reporteDAO;

    public ReporteService(ReporteDAO reporteDAO) {
        this.reporteDAO = reporteDAO;
    }

    public List<ReporteCategoriaDTO> gastosPorCategoria(Integer idUsuario) {
        return reporteDAO.gastosPorCategoria(idUsuario);
    }

    public List<ReporteCategoriaDTO> gastosPorCategoriaUltimosMeses(Integer idUsuario, Integer meses) {
        return reporteDAO.gastosPorCategoriaUltimosMeses(idUsuario, meses);
    }
}