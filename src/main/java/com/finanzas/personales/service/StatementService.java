package com.finanzas.personales.service;

import com.finanzas.personales.dto.request.StatementRequestDTO;
import com.finanzas.personales.model.Statement;
import com.finanzas.personales.model.TarjetaCredito;
import com.finanzas.personales.repository.MovimientoRepository;
import com.finanzas.personales.repository.StatementRepository;
import com.finanzas.personales.repository.TarjetaCreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final TarjetaCreditoRepository tarjetaRepository;
    private final MovimientoRepository movimientoRepository;

    public Statement generar(Long tarjetaId, StatementRequestDTO dto, Long usuarioId) {
        TarjetaCredito t = tarjetaRepository.findById(tarjetaId)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        if (!t.getUsuario().getId().equals(usuarioId)) throw new RuntimeException("Tarjeta no pertenece al usuario");

        LocalDate inicio = dto.getPeriodoInicio();
        LocalDate fin = dto.getPeriodoFin();
        if (inicio == null || fin == null) throw new RuntimeException("Periodo requerido");

        var movimientos = movimientoRepository.findByTarjetaId(tarjetaId).stream()
                .filter(m -> !m.getFecha().isBefore(inicio) && !m.getFecha().isAfter(fin))
                .toList();

        BigDecimal total = movimientos.stream().map(m -> m.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal minimo = total.multiply(BigDecimal.valueOf(0.03)).setScale(2, BigDecimal.ROUND_HALF_UP);

        Statement s = new Statement();
        s.setTarjeta(t);
        s.setPeriodoInicio(inicio);
        s.setPeriodoFin(fin);
        s.setFechaVencimiento(fin.plusDays(t.getDiasVencimiento() != null ? t.getDiasVencimiento() : 0));
        s.setTotal(total);
        s.setMinimo(minimo);
        s.setPagado(BigDecimal.ZERO);
        s.setEstado("ABIERTO");

        return statementRepository.save(s);
    }

    public List<Statement> listarPorTarjeta(Long tarjetaId, Long usuarioId) {
        TarjetaCredito t = tarjetaRepository.findById(tarjetaId).orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        if (!t.getUsuario().getId().equals(usuarioId)) throw new RuntimeException("Tarjeta no pertenece al usuario");
        return statementRepository.findByTarjetaId(tarjetaId);
    }

    public Statement obtener(Long id, Long usuarioId) {
        Statement s = statementRepository.findById(id).orElseThrow(() -> new RuntimeException("Statement no encontrado"));
        if (!s.getTarjeta().getUsuario().getId().equals(usuarioId)) throw new RuntimeException("No pertenece al usuario");
        return s;
    }
}

