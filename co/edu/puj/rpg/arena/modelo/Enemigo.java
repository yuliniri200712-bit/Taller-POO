package co.edu.puj.rpg.arena.modelo;

public class Enemigo extends Personaje {

    public Enemigo(String nombre, int vida, int ataqueBase, int posX, int posY) {
        super(nombre, vida, ataqueBase, posX, posY);
    }

    @Override
    public void defender(int danoRecibido) {
        recibirDano(danoRecibido);
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        objetivo.defender(ataqueBase + 5);
    }

    public boolean estaEnFuria() {
        return vida < vidaMaxima * 0.3;
    }

    @Override
    public char getSimbolo() {
        return 'E';
    }

    @Override
    public String getEstadisticaExtra() {
        return estaEnFuria() ? "FURIA" : "Normal";
    }
}
