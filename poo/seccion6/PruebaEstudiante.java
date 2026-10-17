package poo.seccion6;

// Caso de Estudio 1 corregido: el objeto se instancia con new antes de usarlo
public class PruebaEstudiante {
    public static void main(String[] args) {
        Estudiante estudiante1 = new Estudiante("2024001", "Laura Martínez", 4.35, true);

        String reporte = estudiante1.obtenerReporteAcademicos();
        System.out.println(reporte);
    }
}
