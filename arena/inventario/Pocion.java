package arena.inventario;

public class Pocion {

    private String nombre;
    private int puntosCuracion;

    public Pocion(String nombre, int puntosCuracion) {
        this.nombre = nombre;
        this.puntosCuracion = puntosCuracion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosCuracion() {
        return puntosCuracion;
    }
}
