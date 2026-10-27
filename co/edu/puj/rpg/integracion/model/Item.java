package co.edu.puj.rpg.integracion.model;

import co.edu.puj.rpg.integracion.model.enums.RaridadItem;

import java.util.Objects;

public class Item {
    private final String nombre;
    private final RaridadItem rareza;
    private final double valorBase;

    public Item(String nombre, RaridadItem rareza, double valorBase) {
        this.nombre = nombre;
        this.rareza = rareza;
        this.valorBase = valorBase;
    }

    public double calcularBonificacion() {
        return valorBase * rareza.getMultiplicadorStat();
    }

    public String getNombre() {
        return nombre;
    }

    public RaridadItem getRareza() {
        return rareza;
    }

    public double getValorBase() {
        return valorBase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item otro = (Item) o;
        return nombre.equalsIgnoreCase(otro.nombre) && rareza == otro.rareza;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), rareza);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, +%.2f)", nombre, rareza, calcularBonificacion());
    }
}
