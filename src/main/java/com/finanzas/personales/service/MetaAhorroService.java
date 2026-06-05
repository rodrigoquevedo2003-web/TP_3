package com.finanzas.personales.service;

import com.finanzas.personales.Exception.MetaAhorroInexistenteException;
import com.finanzas.personales.Exception.SaldoInsuficienteException;
import com.finanzas.personales.dto.MovimientoMetaDTO;
import com.finanzas.personales.dto.request.MetaAhorroRequestDTO;
import com.finanzas.personales.dto.response.MetaAhorroResponseDTO;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.MetaAhorro;
import com.finanzas.personales.model.Usuario;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.MetaAhorroRepository;
import com.finanzas.personales.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaAhorroService {

    private final MetaAhorroRepository metaAhorroRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public MetaAhorroResponseDTO crear(MetaAhorroRequestDTO dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MetaAhorro meta = new MetaAhorro();
        meta.setNombre(dto.getNombre());
        meta.setMontoObjetivo(dto.getMontoObjetivo());
        meta.setMontoActual(BigDecimal.ZERO);
        meta.setFechaLimite(dto.getFechaLimite());
        meta.setCumplida(false);
        meta.setUsuario(usuario);

        if (dto.getCuentaId() != null) {
            Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
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
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
            validarCuentaDelUsuario(cuenta, usuarioId);
            meta.setCuenta(cuenta);
        }else{
            meta.setCuenta(null);
        }

        if(meta.getMontoActual().compareTo(meta.getMontoObjetivo()) >= 0){
            meta.setCumplida(true);
        }else{
            meta.setCumplida(false);
        }

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

        return MetaAhorroResponseDTO.from(metaAhorroRepository.save(meta));
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
            throw new RuntimeException("La cuenta no pertenece al usuario");
        }
    }


    private Cuenta resolverCuenta(Long cuentaIdDto, MetaAhorro meta, Long usuarioId){
        if(cuentaIdDto != null){
            Cuenta cuenta = cuentaRepository.findById(cuentaIdDto)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
            validarCuentaDelUsuario(cuenta, usuarioId);
            return cuenta;
        }
        if(meta.getCuenta() != null){
            return meta.getCuenta();
        }
        throw new RuntimeException("Debe indicar una cuenta. La meta no tiene cuenta por defecto asiganada");
    }
}
