package com.finanzas.personales.controller;

import com.finanzas.personales.dto.request.StatementRequestDTO;
import com.finanzas.personales.dto.request.TarjetaRequestDTO;
import com.finanzas.personales.model.Statement;
import com.finanzas.personales.model.TarjetaCredito;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.service.StatementService;
import com.finanzas.personales.service.TarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarjetas")
@RequiredArgsConstructor
public class TarjetaController {

    private final TarjetaService tarjetaService;
    private final StatementService statementService;
    private final com.finanzas.personales.service.MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<TarjetaCredito> crear(@RequestBody TarjetaRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tarjetaService.crear(dto, usuario.getId()));
    }

    @GetMapping
    public ResponseEntity<List<TarjetaCredito>> listar(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(tarjetaService.listar(usuario.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarjetaCredito> obtener(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(tarjetaService.obtener(id, usuario.getId()));
    }

    @PostMapping("/{id}/generar-statement")
    public ResponseEntity<Statement> generarStatement(@PathVariable Long id, @RequestBody StatementRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statementService.generar(id, dto, usuario.getId()));
    }

    @GetMapping("/{id}/statements")
    public ResponseEntity<List<Statement>> listarStatements(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(statementService.listarPorTarjeta(id, usuario.getId()));
    }

    @GetMapping("/{id}/statements/{sId}")
    public ResponseEntity<Object> detalleStatement(@PathVariable Long id, @PathVariable Long sId, @AuthenticationPrincipal Usuario usuario) {
        Statement s = statementService.obtener(sId, usuario.getId());
        var movimientos = movimientoService.listarPorStatement(sId);
        java.math.BigDecimal totalPagado = java.math.BigDecimal.ZERO;
        java.util.List<com.finanzas.personales.dto.response.PagoDTO> pagos = new java.util.ArrayList<>();
        for (var m : movimientos) {
            com.finanzas.personales.dto.response.PagoDTO p = new com.finanzas.personales.dto.response.PagoDTO();
            p.setId(m.getId());
            p.setMonto(m.getMonto());
            p.setFecha(m.getFecha());
            p.setDescripcion(m.getDescripcion());
            p.setCuentaNombre(m.getCuenta() != null ? m.getCuenta().getNombre() : null);
            p.setCategoriaNombre(m.getCategoria() != null ? m.getCategoria().getNombre() : null);
            pagos.add(p);
            totalPagado = totalPagado.add(m.getMonto());
        }

        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        resp.put("statement", s);
        resp.put("pagos", pagos);
        resp.put("totalPagado", totalPagado);
        return ResponseEntity.ok(resp);
    }
}


