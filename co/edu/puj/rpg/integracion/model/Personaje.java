package co.edu.puj.rpg.integracion.model;

import co.edu.puj.rpg.integracion.exception.CapacidadExcedidaException;
import co.edu.puj.rpg.integracion.exception.SinManaException;
import co.edu.puj.rpg.integracion.exception.VidaInsuficienteException;
import co.edu.puj.rpg.integracion.model.enums.EstadoPersonaje;
import co.edu.puj.rpg.integracion.model.enums.TipoPersonaje;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Personaje {
    public static final int CAPACIDAD_MAXIMA_HABILIDADES = 5;
    public static final int CAPACIDAD_MAXIMA_INVENTARIO = 3;
    private static final int DANIO_VENENO_POR_TURNO = 5;

    private final String nombre;
    private final TipoPersonaje tipoPersonaje;
    private EstadoPersonaje estado;
    private int vidaActual;
    private final int vidaMaxima;
    private int manaActual;
    private final int manaMaximo;
    private final int ataqueBase;
    private final int defensaBase;
    private final Set<String> habilidades;
    private final List<Item> inventario;

    public Personaje(String nombre, TipoPersonaje tipoPersonaje) {
        if (tipoPersonaje == null) {
            throw new IllegalArgumentException("El tipo de personaje es obligatorio.");
        }
        this.nombre = nombre;
        this.tipoPersonaje = tipoPersonaje;
        this.estado = EstadoPersonaje.VIVO;
        this.vidaMaxima = 100;
        this.vidaActual = vidaMaxima;
        this.manaMaximo = tipoPersonaje == TipoPersonaje.MAGO ? 80 : 40;
        this.manaActual = manaMaximo;
        this.ataqueBase = 10;
        this.defensaBase = 5;
        this.habilidades = new HashSet<>();
        this.inventario = new ArrayList<>();
    }

    public boolean aprenderHabilidad(String habilidad) throws CapacidadExcedidaException {
        if (habilidad == null || habilidad.isBlank()) {
            return false;
        }
        String normalizada = habilidad.trim();
        if (habilidades.contains(normalizada)) {
            return false;
        }
        if (habilidades.size() >= CAPACIDAD_MAXIMA_HABILIDADES) {
            throw new CapacidadExcedidaException(String.format(
                    "El libro de habilidades de %s está lleno (%d/%d).",
                    nombre, habilidades.size(), CAPACIDAD_MAXIMA_HABILIDADES));
        }
        return habilidades.add(normalizada);
    }

    public Set<String> obtenerHabilidades() {
        return Collections.unmodifiableSet(habilidades);
    }

    public Set<String> getHabilidades() {
        return obtenerHabilidades();
    }

    public void agregarItem(Item item) throws CapacidadExcedidaException {
        if (inventario.size() >= CAPACIDAD_MAXIMA_INVENTARIO) {
            throw new CapacidadExcedidaException(String.format(
                    "El inventario de %s está lleno (%d/%d).",
                    nombre, inventario.size(), CAPACIDAD_MAXIMA_INVENTARIO));
        }
        inventario.add(item);
    }

    public int lanzarHechizo(int costoMana) throws SinManaException {
        if (costoMana > manaActual) {
            throw new SinManaException(String.format(
                    "%s no tiene maná suficiente: requiere %d y tiene %d.",
                    nombre, costoMana, manaActual));
        }
        manaActual -= costoMana;
        return (int) Math.round(calcularAtaque() + costoMana * 0.5);
    }

    public void consumirVida(int costoVida) {
        if (costoVida >= vidaActual) {
            throw new VidaInsuficienteException(String.format(
                    "%s no puede pagar %d de vida: solo tiene %d.",
                    nombre, costoVida, vidaActual));
        }
        vidaActual -= costoVida;
    }

    public int recibirDanio(int danio) {
        int danioFinal = (int) Math.max(0, Math.round(danio - calcularDefensa()));
        vidaActual = Math.max(0, vidaActual - danioFinal);
        if (vidaActual == 0) {
            estado = EstadoPersonaje.DERROTADO;
        }
        return danioFinal;
    }

    public int aplicarEfectosDeEstado() {
        if (estado.recibeDanioPorTurno()) {
            vidaActual = Math.max(0, vidaActual - DANIO_VENENO_POR_TURNO);
            if (vidaActual == 0) {
                estado = EstadoPersonaje.DERROTADO;
            }
            return DANIO_VENENO_POR_TURNO;
        }
        return 0;
    }

    public double calcularAtaque() {
        double total = ataqueBase + tipoPersonaje.getBonificadorAtaqueBase();
        for (Item item : inventario) {
            total += item.calcularBonificacion();
        }
        return total;
    }

    public double calcularDefensa() {
        return defensaBase + tipoPersonaje.getBonificadorDefensaBase();
    }

    public void cambiarEstado(EstadoPersonaje nuevoEstado) {
        if (estado != EstadoPersonaje.DERROTADO && nuevoEstado != null) {
            estado = nuevoEstado;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public TipoPersonaje getTipoPersonaje() {
        return tipoPersonaje;
    }

    public EstadoPersonaje getEstado() {
        return estado;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getManaActual() {
        return manaActual;
    }

    public int getManaMaximo() {
        return manaMaximo;
    }

    public List<Item> getInventario() {
        return Collections.unmodifiableList(inventario);
    }
}
