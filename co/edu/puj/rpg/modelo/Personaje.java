package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {

    // Umbral de experiencia necesario para subir de nivel
    private static final int UMBRAL_EXPERIENCIA = 100;
    // Probabilidad de golpe crítico (15%)
    private static final double PROBABILIDAD_CRITICO = 0.15;

    // Atributos privados: ninguno es accesible directamente desde fuera de la clase
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    // Atributos de apoyo para los retos de expansión
    private int experiencia;
    private int pocionesGrandes;
    private boolean ultimoAtaqueCritico;

    // Constructor completo: inicializa nombre, vidaMaxima, puntosAtaque y oro usando 'this'
    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.puntosAtaque = puntosAtaque;
        this.oro = oro;
        this.nivel = 1;
        this.experiencia = 0;
        this.pocionesGrandes = 0;
        // El estado inicial de la vida pasa por el setter para respetar las validaciones
        setPuntosVida(vidaMaxima);
    }

    // Segundo constructor: solo recibe el nombre y delega en el constructor completo
    public Personaje(String nombre) {
        this(nombre, 100, 15, 50.0);
    }

    // ================= API pública de comportamiento =================

    public void atacar(Enemigo enemigo) {
        if (!this.estaVivo || !enemigo.isEstaVivo()) {
            return;
        }

        int dano = this.puntosAtaque;
        // Golpe crítico: 15% de probabilidad de duplicar el daño
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
            // El setter se encarga de no superar vidaMaxima
            setPuntosVida(this.puntosVida + cantidadRestaurada);
        }
    }

    public void recibirDano(int cantidadDano) {
        if (cantidadDano > 0) {
            // El setter se encarga de no bajar de 0 y de actualizar estaVivo
            setPuntosVida(this.puntosVida - cantidadDano);
        }
    }

    // Tienda: agrega una poción grande al inventario solo si el pago es aceptado por setOro()
    public boolean comprarPocionGrande(double costo) {
        boolean pagoAceptado = setOro(this.oro - costo);
        if (pagoAceptado) {
            this.pocionesGrandes++;
        }
        return pagoAceptado;
    }

    // Consume una poción grande del inventario, si hay alguna disponible
    public boolean usarPocionGrande(int cantidadRestaurada) {
        if (this.pocionesGrandes > 0 && this.estaVivo) {
            this.pocionesGrandes--;
            usarPocion(cantidadRestaurada);
            return true;
        } else {
            return false;
        }
    }

    // Suma experiencia y sube de nivel al superar el umbral, usando el setter protegido
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

    // ================= Setters validados =================

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

    // Retorna false (saldo insuficiente) si el nuevo valor sería negativo, sin alterar el atributo
    public boolean setOro(double oro) {
        if (oro < 0.0) {
            return false;
        } else {
            this.oro = oro;
            return true;
        }
    }

    // Método protegido: el nivel solo se modifica desde la propia jerarquía/paquete del modelo
    protected void setNivel(int nivel) {
        if (nivel >= 1) {
            this.nivel = nivel;
        }
    }

    // ================= Getters (API de lectura) =================

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
