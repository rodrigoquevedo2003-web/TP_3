package com.finanzas.personales.service;

import com.finanzas.personales.Exception.*;
import com.finanzas.personales.dto.CuentaDTO;
import com.finanzas.personales.dto.CuentaUpdateDTO;
import com.finanzas.personales.dto.request.TransferenciaDTO;
import com.finanzas.personales.enums.TipoCuenta;
import com.finanzas.personales.enums.TipoMovimiento;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoService movimientoService;


    public Cuenta crearCuenta(CuentaDTO dto, Usuario usuario) {

        if (dto.getTipoCuenta() == TipoCuenta.EFECTIVO) {

            boolean existeEfectivo =
                    cuentaRepository.existsByUsuarioIdAndTipoCuenta(
                            usuario.getId(),
                            TipoCuenta.EFECTIVO
                    );

            if (existeEfectivo) {
                throw new CuentaEfectivoExistenteException(
                        "El usuario ya tiene una cuenta de efectivo"
                );
            }
        }

        Cuenta cuenta = new Cuenta();

        cuenta.setNombre(dto.getNombre());
        cuenta.setSaldo(dto.getSaldo() != null ? dto.getSaldo() : BigDecimal.ZERO);
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setActiva(true);
        cuenta.setUsuario(usuario);

        return cuentaRepository.save(cuenta);
    }

    public List<Cuenta> listarPorUsuario(Long usuarioId) {
        return cuentaRepository.findByUsuarioId(usuarioId);
    }

    public Cuenta buscarPropia(Long id, Long usuarioId) {
        return cuentaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    public Cuenta actualizarCuenta(Long id, CuentaUpdateDTO dto, Long usuarioId) {
        Cuenta cuenta = buscarPropia(id, usuarioId);

        cuenta.setNombre(dto.getNombre());

        if (cuenta.getTipoCuenta() != TipoCuenta.EFECTIVO && dto.getTipoCuenta() != null) {
            cuenta.setTipoCuenta(dto.getTipoCuenta());
        }

        return cuentaRepository.save(cuenta);
    }

    public void eliminarCuenta(Long id, Long usuarioId) {
        Cuenta cuenta = buscarPropia(id, usuarioId);

        if (cuenta.getTipoCuenta() == TipoCuenta.EFECTIVO) {
            throw new CuentaEfectivoNoEliminableException(
                    "La cuenta de efectivo no se puede eliminar"
            );
        }

        cuentaRepository.delete(cuenta);
    }

    @Transactional
    public void transferir(TransferenciaDTO dto, Long usuarioId) {

        if (dto.getMonto() == null ||dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferenciaInvalidaException(
                    "El monto debe ser mayor a cero"
            );
        }

        if (dto.getCuentaOrigenId() == null || dto.getCuentaDestinoId() == null){
            throw new TransferenciaInvalidaException("Alguna de las cuentas no fue especificada");
        }

        if (dto.getCuentaOrigenId().equals(dto.getCuentaDestinoId())) {
            throw new TransferenciaInvalidaException(
                    "No se puede transferir a la misma cuenta"
            );
        }

        Cuenta origen = cuentaRepository.findById(dto.getCuentaOrigenId())
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta origen no encontrada"));

        if (!origen.getUsuario().getId().equals(usuarioId)) {
            throw new CuentaNoEncontradaException(
                    "La cuenta origen no pertenece al usuario"
            );
        }

        Cuenta destino = cuentaRepository.findById(dto.getCuentaDestinoId())
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta destino no encontrada"));

        if (!destino.getUsuario().getId().equals(usuarioId)) {
            throw new CuentaNoEncontradaException("La cuenta destino no pertenece al usuario");
        }

        if(Boolean.FALSE.equals(origen.getActiva()) || Boolean.FALSE.equals(destino.getActiva())){
            throw new TransferenciaInvalidaException("No es posible operar con una cuenta inactiva");
        }

        if (origen.getSaldo().compareTo(dto.getMonto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }

        origen.setSaldo(origen.getSaldo().subtract(dto.getMonto()));
        destino.setSaldo(destino.getSaldo().add(dto.getMonto()));

        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        movimientoService.registrarMovimientoInterno(origen, TipoMovimiento.EGRESO,
                "Transferencia enviada a " + destino.getNombre(), dto.getMonto());
        movimientoService.registrarMovimientoInterno(destino, TipoMovimiento.INGRESO,
                "Transferencia recibida de " + origen.getNombre(), dto.getMonto());
    }
}