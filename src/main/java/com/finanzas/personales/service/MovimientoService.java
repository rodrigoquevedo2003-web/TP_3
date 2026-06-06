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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final CategoriaRepository categoriaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository,
                             CuentaRepository cuentaRepository,
                             CategoriaRepository categoriaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Movimiento crearMovimiento(MovimientoDTO dto, Long usuarioId) {

        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));

        if (!cuenta.getUsuario().getId().equals(usuarioId)) {
            throw new CuentaNoEncontradaException("La cuenta no pertenece al usuario");
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria no encontrada"));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setCategoria(categoria);
        movimiento.setTipo(dto.getTipo());
        movimiento.setDescripcion(dto.getDescripcion());
        movimiento.setMonto(dto.getMonto());
        movimiento.setFecha(dto.getFecha());
        movimiento.setEsFamiliar(dto.getEsFamiliar());

        if (dto.getTipo() == TipoMovimiento.INGRESO) {
            cuenta.setSaldo(cuenta.getSaldo().add(dto.getMonto()));
        } else if (dto.getTipo() == TipoMovimiento.EGRESO) {
            if (cuenta.getSaldo().compareTo(dto.getMonto()) < 0) {
                throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta");
            }
            cuenta.setSaldo(cuenta.getSaldo().subtract(dto.getMonto()));
        } else {
            throw new RuntimeException("Tipo de movimiento inválido");
        }

        cuentaRepository.save(cuenta);

        return movimientoRepository.save(movimiento);
    }

  /*  public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }*/

    public List<Movimiento> listarMovimientosPorUsuario(Long usuarioId) {
        return movimientoRepository.findByCuentaUsuarioId(usuarioId);
    }

    public Movimiento buscarPorIdYUsuario(Long id, Long usuarioId) {
        return movimientoRepository.findById(id)
                .filter(m -> m.getCuenta().getUsuario().getId().equals(usuarioId))
                .orElseThrow(() -> new MovimientoNoEncontradoException("Movimiento no encontrado o no autorizado"));
    }

    public List<Movimiento> listarPorCuentaYUsuario(Long cuentaId, Long usuarioId) {

        Cuenta cuenta = cuentaRepository.findByIdAndUsuarioId(cuentaId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada o no pertenece a este usuario"));

        return movimientoRepository.findByCuentaId(cuenta.getId());
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
                throw new SaldoInsuficienteException(
                        "No se puede eliminar el ingreso: el saldo actual es menor al monto del movimiento"
                );
            }
            cuenta.setSaldo(cuenta.getSaldo().subtract(movimiento.getMonto()));
        } else {
            cuenta.setSaldo(cuenta.getSaldo().add(movimiento.getMonto()));
        }

        cuentaRepository.save(cuenta);
        movimientoRepository.delete(movimiento);
    }
}