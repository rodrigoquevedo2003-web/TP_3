package com.finanzas.personales.service;


import com.finanzas.personales.Exception.RecursoNoEncontradoException;
import com.finanzas.personales.dto.request.ReglaRecurrenteRequestDTO;
import com.finanzas.personales.model.Categoria;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.model.ReglaRecurrente;
import com.finanzas.personales.repository.CategoriaRepository;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.MovimientoRepository;
import com.finanzas.personales.repository.ReglaRecurrenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;




@Slf4j
@Service
@RequiredArgsConstructor
public class ReglaRecurrenteService {

    private final ReglaRecurrenteRepository reglaRepo;
    private final MovimientoRepository movimientoRepo;
    private final CuentaRepository cuentaRepo;
    private final CategoriaRepository categoriaRepo;


    @Scheduled(cron = "0 0 6 * * *")
    public void ejecutarReglasVencidas() {
        LocalDate hoy = LocalDate.now();
        log.info("[Scheduler] Verificando reglas recurrentes para la fecha: {}", hoy);

        List<ReglaRecurrente> reglasPendientes = reglaRepo
                .findByActivaTrueAndProximaEjecucionLessThanEqual(hoy);

        if (reglasPendientes.isEmpty()) {
            log.info("[Scheduler] No hay reglas pendientes para hoy.");
            return;
        }

        log.info("[Scheduler] Procesando {} regla(s)...", reglasPendientes.size());

        for (ReglaRecurrente regla : reglasPendientes) {
            try {

                procesarReglaIndividual(regla, hoy);
            } catch (org.springframework.dao.DataAccessException e) {
                log.error("[Scheduler] ✗ Error de base de datos en regla ID={}: {}", regla.getId(), e.getMessage());
            } catch (Exception e) {
                log.error("[Scheduler] ✗ Error inesperado en regla ID={}: {}", regla.getId(), e.getMessage(), e);
            }
        }

        log.info("[Scheduler] Finalizado. {} regla(s) procesada(s).", reglasPendientes.size());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void procesarReglaIndividual(ReglaRecurrente regla, LocalDate hoy) {
        crearMovimientoDesdeRegla(regla, hoy);
        avanzarProximaEjecucion(regla, hoy);
        log.info("[Scheduler] ✓ Movimiento creado para regla ID={} | cuenta='{}' | monto={}",
                regla.getId(), regla.getCuenta().getNombre(), regla.getMonto());
    }


    @Transactional
    public ReglaRecurrente crearRegla(ReglaRecurrenteRequestDTO dto, Long usuarioId) {

        Cuenta cuenta = cuentaRepo.findByIdAndUsuarioId(dto.getCuentaId(), usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta no encontrada o no te pertenece (ID: " + dto.getCuentaId() + ")"));


        Categoria categoria = categoriaRepo.findByIdAndUsuarioId(dto.getCategoriaId(), usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada o no te pertenece (ID: " + dto.getCategoriaId() + ")"));

        ReglaRecurrente regla = ReglaRecurrente.builder()
                .cuenta(cuenta)
                .categoria(categoria)
                .tipo(dto.getTipo())
                .descripcion(dto.getDescripcion())
                .monto(dto.getMonto())
                .esFamiliar(dto.getEsFamiliar())
                .frecuenciaDias(dto.getFrecuenciaDias())
                .proximaEjecucion(dto.getProximaEjecucion())
                .activa(true)
                .build();

        return reglaRepo.save(regla);
    }


    @Transactional
    public void desactivarRegla(Long reglaId, Long usuarioId) {
        ReglaRecurrente regla = reglaRepo.findById(reglaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Regla no encontrada: " + reglaId));



        Long duenioId = regla.getCuenta().getUsuario().getId();
        if (!duenioId.equals(usuarioId)) {
            log.warn("[Seguridad] El usuario ID={} intentó desactivar la regla ID={} del usuario ID={}",
                    usuarioId, reglaId, duenioId);
            throw new RecursoNoEncontradoException("No tenés permisos para cancelar esta regla porque no te pertenece.");
        }

        regla.setActiva(false);
        reglaRepo.save(regla);
        log.info("[Service] Regla ID={} desactivada con éxito por su dueño (Usuario ID={})", reglaId, usuarioId);
    }


    @Transactional(readOnly = true)
    public List<ReglaRecurrente> obtenerReglasPorUsuario(Long usuarioId, Long usuarioLogueadoId) {


        if (!usuarioId.equals(usuarioLogueadoId)) {
            throw new IllegalArgumentException("No tenés permiso para ver las reglas de otro usuario.");
        }


        return reglaRepo.findByUsuarioId(usuarioId);
    }

    private void crearMovimientoDesdeRegla(ReglaRecurrente regla, LocalDate fecha) {
        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(regla.getCuenta());
        movimiento.setCategoria(regla.getCategoria());
        movimiento.setTipo(regla.getTipo());
        movimiento.setDescripcion(regla.getDescripcion() + " (automático)");
        movimiento.setMonto(regla.getMonto());
        movimiento.setFecha(fecha);

        movimientoRepo.save(movimiento);
    }

    private void avanzarProximaEjecucion(ReglaRecurrente regla, LocalDate hoy) {
        LocalDate proxima = regla.getProximaEjecucion();
        while (!proxima.isAfter(hoy)) {
            proxima = proxima.plusDays(regla.getFrecuenciaDias());
        }
        regla.setProximaEjecucion(proxima);
        reglaRepo.save(regla);
    }
}