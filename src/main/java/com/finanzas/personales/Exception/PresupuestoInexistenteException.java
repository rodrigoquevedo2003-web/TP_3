package com.finanzas.personales.Exception;

public class PresupuestoInexistenteException extends RuntimeException {
    public PresupuestoInexistenteException(String message) {
        super(message);
    }
}
