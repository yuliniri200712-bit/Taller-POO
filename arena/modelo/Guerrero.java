package arena.modelo;

// Subclase concreta: especialización de combate físico
public class Guerrero extends Personaje {

    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int posX, int posY, int fuerzaEscudo) {
        // super() en la primera línea: inicializa el estado heredado
        super(nombre, vida, ataqueBase, posX, posY);
        this.fuerzaEscudo = fuerzaEscudo;
    }

    // El escudo absorbe parte del daño recibido
    @Override
    public void defender(int danoRecibido) {
        int danoFinal = danoRecibido - fuerzaEscudo;
        if (danoFinal < 0) {
            danoFinal = 0;
        }
        recibirDano(danoFinal);
    }

    // Golpe Demoledor: duplica el ataque base
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
