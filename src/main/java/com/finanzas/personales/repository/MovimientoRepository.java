package com.finanzas.personales.repository;

import com.finanzas.personales.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaId(Long cuentaId);
    List<Movimiento> findByCuentaUsuarioId(Long usuarioId);
    List<Movimiento> findByCategoriaIdAndCuentaUsuarioId(Long categoriaId,Long usuarioId);
    List<Movimiento> findByDeudaIdAndCuentaUsuarioId(Long deudaId, Long usuarioId);
    List<Movimiento> findByDeudaId(Long deudaId);
    List<Movimiento> findByTarjetaIdAndCuentaUsuarioId(Long tarjetaId, Long usuarioId);
    List<Movimiento> findByTarjetaId(Long tarjetaId);
    List<Movimiento> findByStatementId(Long statementId);

}
