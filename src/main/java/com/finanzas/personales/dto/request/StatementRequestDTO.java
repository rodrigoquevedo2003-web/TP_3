package com.finanzas.personales.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatementRequestDTO {
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
}

