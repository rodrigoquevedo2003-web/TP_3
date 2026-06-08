package com.finanzas.personales.service;

import com.finanzas.personales.Exception.UsuarioNoEncontradoException;
import com.finanzas.personales.dto.request.DeudaRequestDTO;
import com.finanzas.personales.dto.response.DeudaResponseDTO;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Deuda;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.DeudaRepository;
import com.finanzas.personales.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeudaService {

    private final DeudaRepository deudaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public DeudaResponseDTO crear(DeudaRequestDTO dto, Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Deuda deuda = new Deuda();

        deuda.setNombre(dto.getNombre());
        deuda.setDescripcion(dto.getDescripcion());
        deuda.setMontoTotal(dto.getMontoTotal());

        deuda.setMontoCuota(
                dto.getMontoTotal()
                        .divide(
                                BigDecimal.valueOf(dto.getCuotasTotales()),
                                2,
                                RoundingMode.HALF_UP
                        )
        );

        deuda.setCuotasTotales(dto.getCuotasTotales());
        deuda.setCuotasPagadas(0);
        deuda.setFechaInicio(dto.getFechaInicio());
        deuda.setSaldada(false);
        deuda.setUsuario(usuario);

        if (dto.getCuentaId() != null) {
            Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            if (!cuenta.getUsuario().getId().equals(usuarioId)) {
                throw new RuntimeException("La cuenta no pertenece al usuario");
            }

            deuda.setCuenta(cuenta);
        }

        return DeudaResponseDTO.from(deudaRepository.save(deuda));
    }

    public List<DeudaResponseDTO> listar(Long usuarioId) {
        return deudaRepository.findByUsuarioIdOrderByFechaInicioAsc(usuarioId)
                .stream()
                .map(DeudaResponseDTO::from)
                .toList();
    }

    public List<DeudaResponseDTO> listarPorEstado(Long usuarioId, Boolean saldada) {
        return deudaRepository.findByUsuarioIdAndSaldadaOrderByFechaInicioAsc(usuarioId, saldada)
                .stream()
                .map(DeudaResponseDTO::from)
                .toList();
    }

    @Transactional
    public DeudaResponseDTO pagarCuota(Long id, Long usuarioId) {

        Deuda deuda = obtenerPropia(id, usuarioId);

        if (deuda.getSaldada()) {
            throw new RuntimeException("La deuda ya está saldada");
        }

        if (deuda.getCuotasPagadas() >= deuda.getCuotasTotales()) {
            deuda.setSaldada(true);
            return DeudaResponseDTO.from(deudaRepository.save(deuda));
        }

        if (deuda.getCuenta() != null) {
            Cuenta cuenta = deuda.getCuenta();

            if (cuenta.getSaldo().compareTo(deuda.getMontoCuota()) < 0) {
                throw new RuntimeException("Saldo insuficiente en la cuenta");
            }

            cuenta.setSaldo(cuenta.getSaldo().subtract(deuda.getMontoCuota()));
            cuentaRepository.save(cuenta);
        }

        deuda.setCuotasPagadas(deuda.getCuotasPagadas() + 1);

        if (deuda.getCuotasPagadas() >= deuda.getCuotasTotales()) {
            deuda.setSaldada(true);
        }

        return DeudaResponseDTO.from(deudaRepository.save(deuda));
    }

    @Transactional
    public void eliminar(Long id, Long usuarioId) {
        Deuda deuda = obtenerPropia(id, usuarioId);
        deudaRepository.delete(deuda);
    }

    public BigDecimal calcularTotalPendiente(Long usuarioId) {
        return deudaRepository.findByUsuarioIdAndSaldadaOrderByFechaInicioAsc(usuarioId, false)
                .stream()
                .map(d -> d.getMontoCuota().multiply(
                        BigDecimal.valueOf(d.getCuotasTotales() - d.getCuotasPagadas())
                ))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Deuda obtenerPropia(Long id, Long usuarioId) {
        return deudaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
    }
}
