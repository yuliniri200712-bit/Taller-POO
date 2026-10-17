package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;

// Sección 5 - Guía de verificación (punto 3): pruebas manuales con valores límite (-10, 0, 100000)
public class PruebasEncapsulamiento {

    public static void main(String[] args) {
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        int[] valoresVida = {-10, 0, 100000, 60};

        System.out.printf("=== PRUEBAS DE setPuntosVida() ===%n");
        for (int valor : valoresVida) {
            heroe.setPuntosVida(valor);
            System.out.printf("setPuntosVida(%d) -> Vida: %d/%d | Vivo: %b%n",
                    valor, heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.isEstaVivo());
        }

        System.out.printf("%n=== PRUEBAS DE setOro() ===%n");
        double[] valoresOro = {-10, 0, 100000};
        for (double valor : valoresOro) {
            boolean aceptado = heroe.setOro(valor);
            System.out.printf("setOro(%.2f) -> Aceptado: %b | Oro: %.2f%n", valor, aceptado, heroe.getOro());
        }

        // setNivel() es protected: desde este paquete no es accesible, se verifica
        // indirectamente observando que el nivel inicial es válido (>= 1).
        System.out.printf("%n=== NIVEL ===%n");
        System.out.printf("Nivel inicial: %d%n", heroe.getNivel());
    }
}
