package co.edu.puj.rpg.arena.modelo;

public abstract class Personaje implements ICombatiente {

    protected String nombre;
    protected int vida;
    protected int ataqueBase;
    protected int posicionX;
    protected int posicionY;

    protected int vidaMaxima;

    public Personaje(String nombre, int vida, int ataqueBase, int posX, int posY) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataqueBase = ataqueBase;
        this.posicionX = posX;
        this.posicionY = posY;
    }

    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    public boolean estaVivo() {
        return vida > 0;
    }

    public void recibirDano(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        vida -= cantidad;
        if (vida < 0) {
            vida = 0;
        }
    }

    public int curar(int cantidad) {
        if (cantidad <= 0 || !estaVivo()) {
            return 0;
        }
        int vidaAntes = vida;
        vida = Math.min(vida + cantidad, vidaMaxima);
        return vida - vidaAntes;
    }

    public void atacar(Personaje p) {
        p.defender(ataqueBase);
    }

    public void atacar(Personaje p, boolean modoFuria) {
        if (modoFuria) {
            p.defender(ataqueBase + ataqueBase / 2);
        } else {
            atacar(p);
        }
    }

    public void setPosicion(int posX, int posY) {
        this.posicionX = posX;
        this.posicionY = posY;
    }

    public abstract char getSimbolo();

    public abstract String getEstadisticaExtra();

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }
}
