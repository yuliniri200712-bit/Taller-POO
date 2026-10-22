package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

public class Enemigo {

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int experienciaOtorgada;
    private boolean estaVivo;

    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque, int experienciaOtorgada) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.puntosAtaque = puntosAtaque;
        this.experienciaOtorgada = experienciaOtorgada;
        setPuntosVida(vidaMaxima);
    }

    public void atacar(Personaje personaje) {
        if (this.estaVivo && personaje.isEstaVivo()) {
            personaje.recibirDano(this.puntosAtaque);
        }
    }

    public void recibirDano(int cantidadDano) {
        if (cantidadDano > 0) {
            setPuntosVida(this.puntosVida - cantidadDano);
        }
    }

    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > this.vidaMaxima) {
            this.puntosVida = this.vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getExperienciaOtorgada() {
        return experienciaOtorgada;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }
}
