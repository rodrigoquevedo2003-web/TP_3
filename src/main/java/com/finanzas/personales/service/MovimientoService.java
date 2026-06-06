package com.finanzas.personales.service;

import com.finanzas.personales.Exception.CategoriaNoEncontradaException;
import com.finanzas.personales.Exception.CuentaNoEncontradaException;
import com.finanzas.personales.Exception.MovimientoNoEncontradoException;
import com.finanzas.personales.Exception.SaldoInsuficienteException;
import com.finanzas.personales.dto.MovimientoDTO;
import com.finanzas.personales.model.Categoria;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.enums.TipoMovimiento;
import com.finanzas.personales.repository.CategoriaRepository;
import com.finanzas.personales.repository.CuentaRepository;
import com.finanzas.personales.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final CategoriaRepository categoriaRepository;


    @Transactional
    public Movimiento crearMovimiento(MovimientoDTO dto, Long usuarioId) {

        Cuenta cuenta = cuentaRepository.findByIdAndUsuarioId(dto.getCuentaId(), usuarioId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria no encontrada"));

        if (dto.getTipo() == TipoMovimiento.EGRESO
                && cuenta.getSaldo().compareTo(dto.getMonto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta");
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setCategoria(categoria);
        movimiento.setTipo(dto.getTipo());
        movimiento.setDescripcion(dto.getDescripcion());
        movimiento.setMonto(dto.getMonto());
        movimiento.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());

        if (dto.getTipo() == TipoMovimiento.INGRESO) {
            cuenta.setSaldo(cuenta.getSaldo().add(dto.getMonto()));
        }else{
            cuenta.setSaldo(cuenta.getSaldo().subtract(dto.getMonto()));
        }

        cuentaRepository.save(cuenta);

        return movimientoRepository.save(movimiento);
    }




    public List<Movimiento> listarMovimientosPorUsuario(Long usuarioId) {
        return movimientoRepository.findByCuentaUsuarioId(usuarioId);
    }



    public Movimiento buscarPorIdYUsuario(Long id, Long usuarioId) {
        return movimientoRepository.findById(id)
                .filter(m -> m.getCuenta().getUsuario().getId().equals(usuarioId))
                .orElseThrow(() -> new MovimientoNoEncontradoException("Movimiento no encontrado"));
    }



    public List<Movimiento> listarPorCuentaYUsuario(Long cuentaId, Long usuarioId) {

        Cuenta cuenta = cuentaRepository.findByIdAndUsuarioId(cuentaId, usuarioId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));

        return movimientoRepository.findByCuentaId(cuentaId);
    }



    public List<Movimiento> listarPorCategoriaYUsuario(Long categoriaId, Long usuarioId) {

        return movimientoRepository.findByCategoriaIdAndCuentaUsuarioId(categoriaId, usuarioId);
    }



    @Transactional
    public void eliminarMovimiento(Long id, Long usuarioId) {

        Movimiento movimiento = buscarPorIdYUsuario(id, usuarioId);
        Cuenta cuenta = movimiento.getCuenta();

        if (movimiento.getTipo() == TipoMovimiento.INGRESO) {

            if (cuenta.getSaldo().compareTo(movimiento.getMonto()) < 0) {
                throw new SaldoInsuficienteException("No se puede eliminar el ingreso: el saldo actual es menor al monto del movimiento");
            }
            cuenta.setSaldo(cuenta.getSaldo().subtract(movimiento.getMonto()));
        } else {
            cuenta.setSaldo(cuenta.getSaldo().add(movimiento.getMonto()));
        }

        cuentaRepository.save(cuenta);
        movimientoRepository.delete(movimiento);
    }


    @Transactional
    public Movimiento editarMovimiento(Long id, MovimientoDTO dto, Long usuarioId) {
        Movimiento movimiento = buscarPorIdYUsuario(id, usuarioId);
        Cuenta cuentaAnterior = movimiento.getCuenta();

        if (movimiento.getTipo() == TipoMovimiento.INGRESO) {
            if (cuentaAnterior.getSaldo().compareTo(movimiento.getMonto()) < 0) {
                throw new SaldoInsuficienteException("No se puede modificar: el saldo actual es menor al monto del movimiento original");
            }
            cuentaAnterior.setSaldo(cuentaAnterior.getSaldo().subtract(movimiento.getMonto()));
        } else {
            cuentaAnterior.setSaldo(cuentaAnterior.getSaldo().add(movimiento.getMonto()));
        }

        Cuenta cuentaNueva;
        if (cuentaAnterior.getId().equals(dto.getCuentaId())) {
            cuentaNueva = cuentaAnterior;
        } else {
            cuentaRepository.save(cuentaAnterior);
            cuentaNueva = cuentaRepository.findByIdAndUsuarioId(dto.getCuentaId(), usuarioId)
                    .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
        }

        if (dto.getTipo() == TipoMovimiento.EGRESO
                && cuentaNueva.getSaldo().compareTo(dto.getMonto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta");
        }

        if (dto.getTipo() == TipoMovimiento.INGRESO) {
            cuentaNueva.setSaldo(cuentaNueva.getSaldo().add(dto.getMonto()));
        } else {
            cuentaNueva.setSaldo(cuentaNueva.getSaldo().subtract(dto.getMonto()));
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria no encontrada"));

        movimiento.setCuenta(cuentaNueva);
        movimiento.setCategoria(categoria);
        movimiento.setTipo(dto.getTipo());
        movimiento.setDescripcion(dto.getDescripcion());
        movimiento.setMonto(dto.getMonto());
        movimiento.setFecha(dto.getFecha() != null ? dto.getFecha() : movimiento.getFecha());

        cuentaRepository.save(cuentaNueva);
        return movimientoRepository.save(movimiento);
    }
}
