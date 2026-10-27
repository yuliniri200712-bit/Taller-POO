package co.edu.puj.rpg.integracion.engine;

import co.edu.puj.rpg.integracion.exception.SinManaException;
import co.edu.puj.rpg.integracion.model.PerfilJugador;
import co.edu.puj.rpg.integracion.model.Personaje;
import co.edu.puj.rpg.integracion.model.enums.EstadoPersonaje;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class CombateEngine {
    private static final int DANIO_CONTRAATAQUE = 18;

    private final Bestiario bestiario;
    private final PerfilJugador perfil;
    private final Map<String, Double> estadisticas;
    private final Random aleatorio;

    public CombateEngine(PerfilJugador perfil) {
        this(perfil, new Random());
    }

    public CombateEngine(PerfilJugador perfil, Random aleatorio) {
        this.perfil = perfil;
        this.aleatorio = aleatorio;
        this.bestiario = new Bestiario();
        this.estadisticas = new TreeMap<String, Double>();
    }

    public String ejecutarAtaqueEspecial(Personaje personaje, int costoMana, int costoVida) throws SinManaException {
        StringBuilder registro = new StringBuilder();
        EstadoPersonaje estado = personaje.getEstado();

        if (estado == EstadoPersonaje.DERROTADO) {
            registro.append(String.format("%s está DERROTADO y no puede combatir.%n", personaje.getNombre()));
            return registro.toString();
        }
        if (!estado.puedeAtacar()) {
            personaje.cambiarEstado(EstadoPersonaje.VIVO);
            actualizarEstadistica("Turnos perdidos por aturdimiento", 1);
            registro.append(String.format("%s está ATURDIDO y pierde su turno. Se recupera para el siguiente.%n",
                    personaje.getNombre()));
            return registro.toString();
        }

        if (costoMana > personaje.getManaActual()) {
            throw new SinManaException(String.format(
                    "%s no tiene maná suficiente: requiere %d y tiene %d.",
                    personaje.getNombre(), costoMana, personaje.getManaActual()));
        }
        if (costoVida > 0) {
            personaje.consumirVida(costoVida);
            actualizarEstadistica("Vida sacrificada", costoVida);
        }
        int danio = personaje.lanzarHechizo(costoMana);
        actualizarEstadistica("Ataques especiales", 1);
        actualizarEstadistica("Daño total infligido", danio);
        actualizarEstadistica("Maná consumido", costoMana);
        registro.append(String.format("%s lanza un ataque especial e inflige %d de daño (maná restante: %d).%n",
                personaje.getNombre(), danio, personaje.getManaActual()));

        double evento = aleatorio.nextDouble();
        if (evento < 0.20) {
            personaje.cambiarEstado(EstadoPersonaje.ENVENENADO);
            registro.append(String.format("El enemigo contraataca con una daga envenenada: %s queda ENVENENADO.%n",
                    personaje.getNombre()));
        } else if (evento < 0.35) {
            personaje.cambiarEstado(EstadoPersonaje.ATURDIDO);
            registro.append(String.format("El enemigo contraataca con un golpe aturdidor: %s queda ATURDIDO.%n",
                    personaje.getNombre()));
        } else {
            int recibido = personaje.recibirDanio(DANIO_CONTRAATAQUE);
            actualizarEstadistica("Daño total recibido", recibido);
            registro.append(String.format("El enemigo contraataca: %s recibe %d de daño tras su defensa.%n",
                    personaje.getNombre(), recibido));
        }

        int veneno = personaje.aplicarEfectosDeEstado();
        if (veneno > 0) {
            actualizarEstadistica("Daño total recibido", veneno);
            registro.append(String.format("El veneno inflige %d de daño a %s.%n", veneno, personaje.getNombre()));
        }
        if (personaje.getEstado() == EstadoPersonaje.DERROTADO) {
            registro.append(String.format("%s ha sido DERROTADO.%n", personaje.getNombre()));
        }
        registro.append(String.format("Estado de %s: %s | Vida: %d/%d%n", personaje.getNombre(),
                personaje.getEstado(), personaje.getVidaActual(), personaje.getVidaMaxima()));
        return registro.toString();
    }

    public boolean registrarVictoria(String nombreEnemigo) {
        boolean esPrimera = bestiario.registrarVictoria(nombreEnemigo);
        actualizarEstadistica("Victorias totales", 1);

        int totalVictorias = bestiario.totalDerrotas();
        if (totalVictorias == 1) {
            perfil.registrarLogro("Primera Victoria");
        }
        if (totalVictorias == 5) {
            perfil.registrarLogro("Veterano de la Arena");
        }
        if (esPrimera) {
            perfil.registrarLogro("Primera victoria contra " + nombreEnemigo.trim());
        }
        if (nombreEnemigo.toLowerCase().contains("drag")) {
            perfil.registrarLogro("Cazador de Dragones");
        }
        return esPrimera;
    }

    private void actualizarEstadistica(String clave, double incremento) {
        if (estadisticas.containsKey(clave)) {
            estadisticas.put(clave, estadisticas.get(clave) + incremento);
        } else {
            estadisticas.put(clave, incremento);
        }
    }

    public Bestiario getBestiario() {
        return bestiario;
    }

    public PerfilJugador getPerfil() {
        return perfil;
    }

    public Map<String, Double> getEstadisticas() {
        return Collections.unmodifiableMap(estadisticas);
    }
}
