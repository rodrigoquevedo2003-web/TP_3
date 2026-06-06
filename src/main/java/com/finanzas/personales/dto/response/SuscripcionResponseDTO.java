package com.finanzas.personales.dto.response;

import com.finanzas.personales.model.Suscripcion;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SuscripcionResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal monto;
    private Boolean activa;
    private int diaCobro;

    public static SuscripcionResponseDTO from(Suscripcion s) {
        SuscripcionResponseDTO dto = new SuscripcionResponseDTO();
        dto.setId(s.getId());
        dto.setNombre(s.getNombre());
        dto.setMonto(s.getMonto());
        dto.setActiva(s.getActiva());
        dto.setDiaCobro(s.getDiaCobro());
        return dto;
    }
}
