package com.finanzas.personales.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ResumenDeudasDTO {
    private int totalDeudas;
    private BigDecimal totalPendiente;
    private Map<String, Integer> countPorTipo;
    private Map<String, BigDecimal> pendientePorTipo;
}

