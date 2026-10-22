package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {

    private static final int UMBRAL_EXPERIENCIA = 100;

    private static final double PROBABILIDAD_CRITICO = 0.15;

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    private int experiencia;
    private int pocionesGrandes;
    private boolean ultimoAtaqueCritico;

    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.puntosAtaque = puntosAtaque;
        this.oro = oro;
        this.nivel = 1;
        this.experiencia = 0;
        this.pocionesGrandes = 0;

        setPuntosVida(vidaMaxima);
    }

    public Personaje(String nombre) {
        this(nombre, 100, 15, 50.0);
    }

    public void atacar(Enemigo enemigo) {
        if (!this.estaVivo || !enemigo.isEstaVivo()) {
            return;
        }

        int dano = this.puntosAtaque;

        this.ultimoAtaqueCritico = Math.random() < PROBABILIDAD_CRITICO;
        if (this.ultimoAtaqueCritico) {
            dano = dano * 2;
        }

        enemigo.recibirDano(dano);

        if (!enemigo.isEstaVivo()) {
            ganarExperiencia(enemigo.getExperienciaOtorgada());
        }
    }

    public void usarPocion(int cantidadRestaurada) {
        if (this.estaVivo && cantidadRestaurada > 0) {
            setPuntosVida(this.puntosVida + cantidadRestaurada);
        }
    }

    public void recibirDano(int cantidadDano) {
        if (cantidadDano > 0) {
            setPuntosVida(this.puntosVida - cantidadDano);
        }
    }

    public boolean comprarPocionGrande(double costo) {
        boolean pagoAceptado = setOro(this.oro - costo);
        if (pagoAceptado) {
            this.pocionesGrandes++;
        }
        return pagoAceptado;
    }

    public boolean usarPocionGrande(int cantidadRestaurada) {
        if (this.pocionesGrandes > 0 && this.estaVivo) {
            this.pocionesGrandes--;
            usarPocion(cantidadRestaurada);
            return true;
        } else {
            return false;
        }
    }

    private void ganarExperiencia(int puntos) {
        if (puntos <= 0) {
            return;
        }
        this.experiencia += puntos;
        while (this.experiencia >= UMBRAL_EXPERIENCIA) {
            this.experiencia -= UMBRAL_EXPERIENCIA;
            setNivel(this.nivel + 1);
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

    public boolean setOro(double oro) {
        if (oro < 0.0) {
            return false;
        } else {
            this.oro = oro;
            return true;
        }
    }

    protected void setNivel(int nivel) {
        if (nivel >= 1) {
            this.nivel = nivel;
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

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getPocionesGrandes() {
        return pocionesGrandes;
    }

    public boolean isUltimoAtaqueCritico() {
        return ultimoAtaqueCritico;
    }
}
