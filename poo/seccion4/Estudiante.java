package poo.seccion4;

public class Estudiante {

    // Atributos sin inicializar: el estado inicial lo asignan los constructores
    private String identificacion;
    private String nombreCompleto;
    private double promedioCalificaciones;
    private boolean estaMatriculado;

    public Estudiante(String identificacion, String nombreCompleto, double promedioCalificaciones, boolean estaMatriculado) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.promedioCalificaciones = promedioCalificaciones;
        this.estaMatriculado = estaMatriculado;
    }

    // Construye el reporte sin imprimirlo; la impresión es responsabilidad de la clase ejecutable
    public String obtenerReporteAcademicos() {
        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("ID: %s | ", identificacion));
        reporte.append(String.format("Nombre: %s | ", nombreCompleto));
        reporte.append(String.format("Promedio: %.2f | ", promedioCalificaciones));
        reporte.append(String.format("Matriculado: %b", estaMatriculado));
        return reporte.toString();
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public double getPromedioCalificaciones() {
        return promedioCalificaciones;
    }

    public boolean isEstaMatriculado() {
        return estaMatriculado;
    }
}
