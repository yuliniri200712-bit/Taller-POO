package co.edu.puj.rpg.relaciones;

public class Arma {
    private String nombre;
    private int danioBase;

    public Arma(String nombre, int danioBase) {
        this.nombre = nombre;
        this.danioBase = danioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanioBase() {
        return danioBase;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Arma[nombre=").append(nombre);
        sb.append(", danioBase=").append(danioBase).append("]");
        return sb.toString();
    }
}
