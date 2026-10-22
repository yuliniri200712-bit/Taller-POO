package co.edu.puj.rpg.arena.modelo;

/*
 * ¿Por qué una clase abstracta y no solo una interfaz?
 * ICombatiente define un contrato de comportamiento puro (defender()). Personaje,
 * en cambio, necesita guardar estado común (nombre, vida, posiciones) y ofrecer
 * comportamiento concreto reutilizable (estaVivo(), recibirDano()). Las interfaces
 * no pueden tener atributos de instancia, por eso la clase abstracta es la
 * herramienta adecuada para no duplicar estado ni métodos base en las subclases.
 */
public abstract class Personaje implements ICombatiente {

    protected String nombre;
    protected int vida;
    protected int ataqueBase;
    protected int posicionX;
    protected int posicionY;
    // Tope de salud para que las curaciones no superen la vida inicial
    protected int vidaMaxima;

    public Personaje(String nombre, int vida, int ataqueBase, int posX, int posY) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataqueBase = ataqueBase;
        this.posicionX = posX;
        this.posicionY = posY;
    }

    // Método abstracto: cada subclase define su propia lógica de ataque único
    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    // ================= Métodos concretos compartidos =================

    public boolean estaVivo() {
        return vida > 0;
    }

    // Descuenta la salud garantizando que no descienda de cero
    public void recibirDano(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        vida -= cantidad;
        if (vida < 0) {
            vida = 0;
        }
    }

    // Recupera salud sin superar la vida máxima; retorna lo realmente curado
    public int curar(int cantidad) {
        if (cantidad <= 0 || !estaVivo()) {
            return 0;
        }
        int vidaAntes = vida;
        vida = Math.min(vida + cantidad, vidaMaxima);
        return vida - vidaAntes;
    }

    // Sobrecarga de métodos (polimorfismo estático): ataque básico
    public void atacar(Personaje p) {
        p.defender(ataqueBase);
    }

    // Sobrecarga de métodos: en modo furia el ataque básico aumenta un 50%
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

    // Símbolo con el que se representa en el tablero 2D
    public abstract char getSimbolo();

    // Recurso secundario para las estadísticas (maná, escudo, etc.)
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
