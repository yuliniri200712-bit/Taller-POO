package poo.seccion6;

public class Vehiculo {
    private int velocidadMaxima;
    private boolean encendido;

    public Vehiculo(int velocidadMaxima, boolean encendido) {
        this.velocidadMaxima = velocidadMaxima;
        this.encendido = encendido;
    }

    public void imprimirEstado() {
        System.out.printf("Velocidad Máxima: %d | Encendido: %b%n", velocidadMaxima, encendido);
    }

    public static void main(String[] args) {
        Vehiculo v = new Vehiculo(200, true);
        v.imprimirEstado();
    }
}
