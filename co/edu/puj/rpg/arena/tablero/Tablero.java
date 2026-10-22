package co.edu.puj.rpg.arena.tablero;

import co.edu.puj.rpg.arena.modelo.Personaje;

import java.util.List;

public class Tablero {

    public static final int TAMANIO = 5;
    public static final char VACIO = '.';
    public static final char COFRE = 'C';

    private char[][] tablero = new char[TAMANIO][TAMANIO];
    private int cofreX;
    private int cofreY;

    public Tablero(int cofreX, int cofreY) {
        this.cofreX = cofreX;
        this.cofreY = cofreY;
        limpiar();
    }

    private void limpiar() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = VACIO;
            }
        }
        tablero[cofreX][cofreY] = COFRE;
    }

    public void actualizar(List<Personaje> heroes, List<Personaje> enemigos) {
        limpiar();
        colocar(heroes);
        colocar(enemigos);
    }

    private void colocar(List<Personaje> personajes) {
        for (Personaje p : personajes) {
            if (p.estaVivo() && estaDentro(p.getPosicionX(), p.getPosicionY())) {
                tablero[p.getPosicionX()][p.getPosicionY()] = p.getSimbolo();
            }
        }
    }

    public boolean estaDentro(int fila, int columna) {
        return fila >= 0 && fila < TAMANIO && columna >= 0 && columna < TAMANIO;
    }

    public boolean esTransitable(int fila, int columna) {
        return estaDentro(fila, columna)
                && (tablero[fila][columna] == VACIO || tablero[fila][columna] == COFRE);
    }

    public boolean esCofre(int fila, int columna) {
        return fila == cofreX && columna == cofreY;
    }

    public void mostrarTablero(char[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print("[" + matriz[i][j] + "]");
            }
            System.out.println();
        }
    }

    public void mostrar() {
        mostrarTablero(tablero);
    }

    public char[][] getMatriz() {
        return tablero;
    }
}
