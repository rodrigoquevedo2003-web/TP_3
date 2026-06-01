package com.finanzas.personales.service;

import com.finanzas.personales.dto.MovimientoDTO;
import com.finanzas.personales.model.Categoria;
import com.finanzas.personales.model.Cuenta;
import com.finanzas.personales.model.Movimiento;
import com.finanzas.personales.model.TipoMovimiento;
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
    public Movimiento crearMovimiento(MovimientoDTO dto) {

        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

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
            cuenta.setSaldo(cuenta.getSaldo().subtract(dto.getMonto()));
        } else {
            throw new RuntimeException("Tipo de movimiento inválido");
        }

        cuentaRepository.save(cuenta);

        return movimientoRepository.save(movimiento);
    }

    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento buscarPorId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    public List<Movimiento> listarPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    public List<Movimiento> listarPorCategoria(Long categoriaId) {
        return movimientoRepository.findByCategoriaId(categoriaId);
    }

    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = buscarPorId(id);
        movimientoRepository.delete(movimiento);
    }
}