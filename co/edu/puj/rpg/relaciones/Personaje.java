package co.edu.puj.rpg.relaciones;

public class Personaje {
    private String nombre;
    private int nivel;
    private Arma armaEquipada;
    private Gremio gremio;
    private final CorazonRPG corazon;

    public Personaje(String nombre, int nivel) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.armaEquipada = null;
        this.gremio = null;
        this.corazon = new CorazonRPG(100 + nivel * 10, 50 + nivel * 5);
    }

    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) {
            System.out.printf("%s no puede equipar un arma inexistente.%n", nombre);
            return;
        }
        this.armaEquipada = nuevaArma;
        System.out.printf("%s ha equipado el arma %s (daño base: %d).%n",
                nombre, nuevaArma.getNombre(), nuevaArma.getDanioBase());
    }

    public void desequiparArma() {
        if (this.armaEquipada != null) {
            String nombreArma = this.armaEquipada.getNombre();
            this.armaEquipada = null;
            System.out.printf("%s ha retirado el arma %s.%n", nombre, nombreArma);
        } else {
            System.out.printf("%s no tiene ningún arma para retirar.%n", nombre);
        }
    }

    public int calcularAtaqueTotal() {
        int ataqueBase = this.nivel * 5;
        if (this.armaEquipada != null) {
            ataqueBase += this.armaEquipada.getDanioBase();
        }
        return ataqueBase;
    }

    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null || nuevoGremio == this.gremio) {
            return;
        }
        if (this.gremio != null) {
            salirDeGremio();
        }
        this.gremio = nuevoGremio;
        nuevoGremio.agregarMiembro(this);
    }

    public void salirDeGremio() {
        if (this.gremio != null) {
            Gremio gremioAnterior = this.gremio;
            this.gremio = null;
            gremioAnterior.removerMiembro(this);
        }
    }

    public void recibirDanio(int cantidad) {
        corazon.recibirDanio(cantidad);
    }

    public void curar(int cantidad) {
        corazon.curar(cantidad);
    }

    public boolean lanzarHechizo(int costoMana) {
        return corazon.consumirMana(costoMana);
    }

    public String getNombre() {
        return nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public Gremio getGremio() {
        return gremio;
    }

    public int getSalud() {
        return corazon.getSaludBase();
    }

    public int getMana() {
        return corazon.getManaBase();
    }

    public String obtenerEstadoCorazon() {
        return corazon.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Personaje[nombre=").append(nombre);
        sb.append(", nivel=").append(nivel);
        sb.append(", arma=").append(armaEquipada != null ? armaEquipada.getNombre() : "Desarmado");
        sb.append(", gremio=").append(gremio != null ? gremio.getNombre() : "Sin gremio");
        sb.append(", ").append(corazon);
        sb.append(", ataqueTotal=").append(calcularAtaqueTotal()).append("]");
        return sb.toString();
    }
}
