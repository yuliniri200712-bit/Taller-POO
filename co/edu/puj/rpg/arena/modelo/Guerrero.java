package co.edu.puj.rpg.arena.modelo;

public class Guerrero extends Personaje {

    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int posX, int posY, int fuerzaEscudo) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.fuerzaEscudo = fuerzaEscudo;
    }

    @Override
    public void defender(int danoRecibido) {
        int danoFinal = danoRecibido - fuerzaEscudo;
        if (danoFinal < 0) {
            danoFinal = 0;
        }
        recibirDano(danoFinal);
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        objetivo.defender(ataqueBase * 2);
    }

    @Override
    public char getSimbolo() {
        return 'G';
    }

    @Override
    public String getEstadisticaExtra() {
        return String.format("Escudo: %d", fuerzaEscudo);
    }

    public int getFuerzaEscudo() {
        return fuerzaEscudo;
    }
}
