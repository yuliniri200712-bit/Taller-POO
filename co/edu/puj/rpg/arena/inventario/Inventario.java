package co.edu.puj.rpg.arena.inventario;

import java.util.ArrayList;
import java.util.List;

/*
 * Contenedor genérico parametrizado sobre <T>.
 * - Seguridad de tipos: Inventario<Pocion> solo acepta objetos Pocion; el
 *   compilador rechaza cualquier otro tipo.
 * - Sin casteo explícito: obtener() ya retorna T, así que Pocion p = inventario.obtener(0);
 */
public class Inventario<T> {

    private List<T> items = new ArrayList<>();

    public void agregar(T item) {
        items.add(item);
    }

    public T obtener(int index) {
        return items.get(index);
    }

    public List<T> getItems() {
        return items;
    }

    // Retira y retorna el ítem; el llamador valida antes el índice para evitar IndexOutOfBoundsException
    public T remover(int index) {
        return items.remove(index);
    }

    public int tamanio() {
        return items.size();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}
