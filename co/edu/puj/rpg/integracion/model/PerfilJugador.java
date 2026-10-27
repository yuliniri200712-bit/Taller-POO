package co.edu.puj.rpg.integracion.model;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class PerfilJugador {
    private final String nombreJugador;
    private final TreeSet<String> logros;

    public PerfilJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        this.logros = new TreeSet<>();
    }

    public boolean registrarLogro(String logro) {
        if (logro == null || logro.isBlank()) {
            return false;
        }
        return logros.add(logro.trim());
    }

    public Set<String> listarLogrosOrdenados() {
        return Collections.unmodifiableSet(logros);
    }

    public String getNombreJugador() {
        return nombreJugador;
    }
}
