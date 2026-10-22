package co.edu.puj.rpg.arena.modelo;

// Subclase concreta para los entes enemigos controlados por la IA
public class Enemigo extends Personaje {

    public Enemigo(String nombre, int vida, int ataqueBase, int posX, int posY) {
        super(nombre, vida, ataqueBase, posX, posY);
    }

    // Sin armadura: recibe el daño completo
    @Override
    public void defender(int danoRecibido) {
        recibirDano(danoRecibido);
    }

    // Embestida: daño del ataque base más 5 puntos
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        objetivo.defender(ataqueBase + 5);
    }

    // Entra en modo furia cuando su vida baja del 30%
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
