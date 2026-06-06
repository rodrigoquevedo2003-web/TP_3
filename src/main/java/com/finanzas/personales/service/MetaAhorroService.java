package com.finanzas.personales.service;

import com.finanzas.personales.Exception.CuentaNoEncontradaException;
import com.finanzas.personales.Exception.MetaAhorroInexistenteException;
import com.finanzas.personales.Exception.SaldoInsuficienteException;
import com.finanzas.personales.Exception.UsuarioNoEncontradoException;
import com.finanzas.personales.dto.MovimientoMetaDTO;
import com.finanzas.personales.dto.request.MetaAhorroRequestDTO;
import com.finanzas.personales.dto.response.MetaAhorroResponseDTO;
import com.finanzas.personales.enums.TipoMovimiento;
import com.finanzas.personales.model.*;
import com.finanzas.personales.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaAhorroService {

    private final MetaAhorroRepository metaAhorroRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public MetaAhorroResponseDTO crear(MetaAhorroRequestDTO dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        MetaAhorro meta = new MetaAhorro();
        meta.setNombre(dto.getNombre());
        meta.setMontoObjetivo(dto.getMontoObjetivo());
        meta.setMontoActual(BigDecimal.ZERO);
        meta.setFechaLimite(dto.getFechaLimite());
        meta.setCumplida(false);
        meta.setUsuario(usuario);

        if (dto.getCuentaId() != null) {
            Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                    .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
            validarCuentaDelUsuario(cuenta, usuarioId);
            meta.setCuenta(cuenta);
        }

        return MetaAhorroResponseDTO.from(metaAhorroRepository.save(meta));
    }

    public List<MetaAhorroResponseDTO> listarPorUsuario(Long usuarioId) {
        return metaAhorroRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(MetaAhorroResponseDTO::from)
                .toList();
    }

    public List<MetaAhorroResponseDTO> listarPorUsuarioYEstado(Long usuarioId, Boolean cumplida){
        return metaAhorroRepository.findByUsuarioIdAndCumplida(usuarioId, cumplida)
                .stream()
                .map(MetaAhorroResponseDTO::from)
                .toList();
    }

    public MetaAhorroResponseDTO buscarPorId(Long metaId, Long usuarioId){
        MetaAhorro meta = obtenerMetaDelUsuario(metaId, usuarioId);
        return MetaAhorroResponseDTO.from(meta);
    }

    @Transactional
    public MetaAhorroResponseDTO actualizar(Long metaId, MetaAhorroRequestDTO dto, Long usuarioId){
        MetaAhorro meta = obtenerMetaDelUsuario(metaId, usuarioId);

        meta.setNombre(dto.getNombre());
        meta.setMontoObjetivo(dto.getMontoObjetivo());
        meta.setFechaLimite(dto.getFechaLimite());

        if(dto.getCuentaId() != null){
            Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                    .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
            validarCuentaDelUsuario(cuenta, usuarioId);
            meta.setCuenta(cuenta);
        }else{
            meta.setCuenta(null);
        }

        meta.setCumplida(meta.getMontoActual().compareTo(meta.getMontoObjetivo()) >= 0);

        return MetaAhorroResponseDTO.from(metaAhorroRepository.save(meta));
    }

    @Transactional
    public void eliminar(Long metaId, Long usuarioId){
        MetaAhorro meta = obtenerMetaDelUsuario(metaId, usuarioId);
        metaAhorroRepository.delete(meta);
    }


    @Transactional
    public MetaAhorroResponseDTO depositar(Long metaId, MovimientoMetaDTO dto, Long usuarioId){
        MetaAhorro meta = obtenerMetaDelUsuario(metaId, usuarioId);
        Cuenta cuenta = resolverCuenta(dto.getCuentaId(), meta, usuarioId);

        BigDecimal monto = dto.getMonto();

        if(cuenta.getSaldo().compareTo(monto) < 0){
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta. Disponible: " + cuenta.getSaldo());
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
        cuentaRepository.save(cuenta);

        meta.depositar(monto);

        registrarMovimientoMeta(cuenta, monto, TipoMovimiento.EGRESO, "Deposito en meta: " + meta.getNombre(), usuarioId);

        return MetaAhorroResponseDTO.from(metaAhorroRepository.save(meta));
    }


    @Transactional
    public MetaAhorroResponseDTO retirar(Long metaId, MovimientoMetaDTO dto, Long usuarioId){
        MetaAhorro meta = obtenerMetaDelUsuario(metaId, usuarioId);
        Cuenta cuenta = resolverCuenta(dto.getCuentaId(), meta, usuarioId);

        BigDecimal monto = dto.getMonto();

        meta.retirar(monto);

        cuenta.setSaldo(cuenta.getSaldo().add(monto));
        cuentaRepository.save(cuenta);

        registrarMovimientoMeta(cuenta, monto, TipoMovimiento.INGRESO, "Retiro de meta: " + meta.getNombre(), usuarioId);

        return MetaAhorroResponseDTO.from(metaAhorroRepository.save(meta));
    }


    private void registrarMovimientoMeta(Cuenta cuenta, BigDecimal monto, TipoMovimiento tipo, String descripcion, Long usuarioId){
        Categoria categoria = categoriaRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(c -> c.getNombre().toLowerCase().contains("ahorro") || c.getNombre().toLowerCase().contains("meta"))
                .findFirst()
                .orElse(categoriaRepository.findByUsuarioId(usuarioId)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("El usuario no tiene categorias disponibles")));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setCategoria(categoria);
        movimiento.setTipo(tipo);
        movimiento.setDescripcion(descripcion);
        movimiento.setMonto(monto);
        movimiento.setFecha(LocalDate.now());

        movimientoRepository.save(movimiento);
    }


    private MetaAhorro obtenerMetaDelUsuario(Long metaId, Long usuarioId){
        MetaAhorro meta = metaAhorroRepository.findById(metaId)
                .orElseThrow(() -> new MetaAhorroInexistenteException("Meta de ahorro no encontrada"));

        if(!meta.getUsuario().getId().equals(usuarioId)){
            throw new MetaAhorroInexistenteException("Meta de ahorro no encontrada");
        }
        return meta;
    }

    private void validarCuentaDelUsuario(Cuenta cuenta, Long usuarioId) {
        if(!cuenta.getUsuario().getId().equals(usuarioId)) {
            throw new CuentaNoEncontradaException("La cuenta no pertenece al usuario");
        }
    }


    private Cuenta resolverCuenta(Long cuentaIdDto, MetaAhorro meta, Long usuarioId){
        if(cuentaIdDto != null){
            Cuenta cuenta = cuentaRepository.findById(cuentaIdDto)
                    .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
            validarCuentaDelUsuario(cuenta, usuarioId);
            return cuenta;
        }
        if(meta.getCuenta() != null){
            return meta.getCuenta();
        }
        throw new CuentaNoEncontradaException("Debe indicar una cuenta. La meta no tiene cuenta por defecto asiganada");
    }
}
