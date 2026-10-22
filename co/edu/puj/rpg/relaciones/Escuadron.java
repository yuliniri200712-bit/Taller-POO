package co.edu.puj.rpg.relaciones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Escuadron {
    private String nombre;
    private List<Personaje> miembros;

    public Escuadron(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    public void reclutarPersonaje(Personaje p) {
        if (p != null && !miembros.contains(p)) {
            miembros.add(p);
        }
    }

    public void expulsarPersonaje(Personaje p) {
        if (p != null) {
            miembros.remove(p);
        }
    }

    public boolean contiene(Personaje p) {
        return miembros.contains(p);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Escuadron[nombre=").append(nombre);
        sb.append(", miembros=[");
        for (int i = 0; i < miembros.size(); i++) {
            sb.append(miembros.get(i).getNombre());
            if (i < miembros.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]]");
        return sb.toString();
    }
}
