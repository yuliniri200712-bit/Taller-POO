package co.edu.puj.rpg.arena.modelo;

// Subclase concreta: especialización de combate mágico
public class Mago extends Personaje {

    private static final int COSTO_BOLA_FUEGO = 20;
    private static final int COSTO_BARRERA = 10;

    protected int puntosMana;

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.puntosMana = puntosMana;
    }

    // Barrera mágica: si tiene maná suficiente, reduce el daño a la mitad
    @Override
    public void defender(int danoRecibido) {
        if (puntosMana >= COSTO_BARRERA) {
            puntosMana -= COSTO_BARRERA;
            recibirDano(danoRecibido / 2);
        } else {
            recibirDano(danoRecibido);
        }
    }

    // Bola de Fuego: consume maná y duplica el daño; sin maná lanza un ataque básico
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (puntosMana >= COSTO_BOLA_FUEGO) {
            puntosMana -= COSTO_BOLA_FUEGO;
            objetivo.defender(ataqueBase * 2);
        } else {
            atacar(objetivo);
        }
    }

    @Override
    public char getSimbolo() {
        return 'M';
    }

    @Override
    public String getEstadisticaExtra() {
        return String.format("Maná: %d", puntosMana);
    }

    public int getPuntosMana() {
        return puntosMana;
    }
}
