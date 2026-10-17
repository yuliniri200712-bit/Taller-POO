package poo.seccion4;

/*
 * Impacto de elegir Wrappers en lugar de tipos primitivos:
 *
 * - Consumo de memoria: un primitivo (double, int, boolean) guarda el valor
 *   directamente en el campo del objeto (8, 4 y ~1 byte respectivamente).
 *   Un Wrapper (Double, Integer, Boolean) es un objeto aparte en el heap:
 *   el campo solo guarda una referencia (4-8 bytes) y el objeto envoltorio
 *   añade su cabecera (~12-16 bytes) más el valor. Además, el autoboxing y
 *   unboxing generan objetos y costos de procesamiento extra.
 *
 * - Valores nulos: los primitivos nunca pueden ser null; siempre tienen un
 *   valor por defecto (0, 0.0, false). Los Wrappers sí admiten null, lo cual
 *   permite representar "dato desconocido / no asignado", pero obliga a
 *   validar antes de operar para evitar una NullPointerException al hacer
 *   unboxing.
 */
public class Vehiculo {

    private String marca;
    private String modelo;

    // Se usa el primitivo double: el kilometraje siempre tiene un valor
    // (como mínimo 0.0), no tiene sentido que sea null y el primitivo
    // ocupa menos memoria y evita el costo de autoboxing en los cálculos.
    private double kilometraje;

    // Se usa el primitivo int: todo vehículo tiene una velocidad máxima
    // conocida, nunca "ausente", por lo que no se necesita admitir null.
    private int velocidadMaxima;

    // Se usa el primitivo boolean: el motor solo puede estar encendido o
    // apagado; un tercer estado null no aporta significado y sería riesgoso.
    private boolean encendido;

    // Constructor por defecto
    public Vehiculo() {
        this.marca = "Genérico";
        this.modelo = "Sin Modelo";
        this.kilometraje = 0.0;
        this.velocidadMaxima = 120;
        this.encendido = false;
    }

    // Constructor parametrizado
    public Vehiculo(String marca, String modelo, double kilometraje, int velocidadMaxima, boolean encendido) {
        this.marca = marca;
        this.modelo = modelo;
        this.kilometraje = kilometraje;
        this.velocidadMaxima = velocidadMaxima;
        this.encendido = encendido;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getKilometraje() {
        return kilometraje;
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public boolean isEncendido() {
        return encendido;
    }
}
