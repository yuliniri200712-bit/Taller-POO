package co.edu.puj.rpg.integracion.model.enums;

public enum EstadoPersonaje {
    VIVO,
    DERROTADO,
    ENVENENADO,
    ATURDIDO;

    public boolean puedeAtacar() {
        return this == VIVO || this == ENVENENADO;
    }

    public boolean recibeDanioPorTurno() {
        return this == ENVENENADO;
    }
}
