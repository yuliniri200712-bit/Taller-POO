package arena.principal;

import arena.inventario.Inventario;
import arena.inventario.Pocion;
import arena.modelo.Enemigo;
import arena.modelo.Guerrero;
import arena.modelo.Mago;
import arena.modelo.Personaje;
import arena.tablero.Tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int COFRE_X = 2;
    private static final int COFRE_Y = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ================= Inicialización =================
        Tablero tablero = new Tablero(COFRE_X, COFRE_Y);

        // Colecciones dinámicas declaradas con la referencia abstracta List
        List<Personaje> escuadron = new ArrayList<>();
        escuadron.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        escuadron.add(new Mago("Gandalf", 80, 25, 0, 1, 50));

        List<Personaje> enemigos = new ArrayList<>();
        enemigos.add(new Enemigo("Orco", 70, 18, 0, 4));
        enemigos.add(new Enemigo("Goblin", 50, 12, 4, 0));
        enemigos.add(new Enemigo("Troll", 90, 20, 4, 4));

        // Contenedor genérico: solo admite Pocion, sin necesidad de casteos
        Inventario<Pocion> inventario = new Inventario<>();
        inventario.agregar(new Pocion("Poción Menor", 25));
        inventario.agregar(new Pocion("Poción Mayor", 50));
        inventario.agregar(new Pocion("Poción Menor", 25));

        // El héroe principal es el primero del escuadrón (Thorin)
        Personaje heroePrincipal = escuadron.get(0);
        boolean cofreAlcanzado = false;
        int turno = 0;

        System.out.printf("============ ARENA RPG ============%n");
        System.out.printf("Escuadrón: %d héroes | Enemigos: %d%n", escuadron.size(), enemigos.size());
        System.out.printf("Objetivo: eliminar a todos los enemigos o llevar a %s al cofre [C] (%d,%d).%n",
                heroePrincipal.getNombre(), COFRE_X, COFRE_Y);

        // ================= Ciclo principal de batalla =================
        do {
            turno++;

            // Fase de renderizado y estado
            tablero.actualizar(escuadron, enemigos);
            System.out.printf("%n=============== TURNO %d ===============%n", turno);
            tablero.mostrar();
            mostrarEstadisticas(escuadron, enemigos, inventario);

            // Turno del jugador: recorrido for-each sobre cada héroe vivo
            for (Personaje heroe : escuadron) {
                if (!heroe.estaVivo() || !hayVivos(enemigos)) {
                    continue;
                }

                boolean accionRealizada = false;
                while (!accionRealizada) {
                    System.out.printf("%n--- Turno de %s (%c) | Vida: %d/%d | %s ---%n",
                            heroe.getNombre(), heroe.getSimbolo(), heroe.getVida(), heroe.getVidaMaxima(),
                            heroe.getEstadisticaExtra());
                    System.out.printf("1. Atacar%n2. Usar Inventario%n3. Mover en Tablero%n");
                    System.out.printf("Seleccione una opción: ");
                    int opcion = leerEntero(scanner);

                    switch (opcion) {
                        case 1:
                            accionRealizada = atacar(scanner, heroe, enemigos);
                            break;
                        case 2:
                            accionRealizada = usarInventario(scanner, heroe, inventario);
                            break;
                        case 3:
                            accionRealizada = mover(scanner, heroe, tablero);
                            break;
                        default:
                            System.out.printf("[ERROR] Opción inválida. Ingrese 1, 2 o 3.%n");
                    }
                }

                // Todo cambio se refleja de inmediato en la matriz del mapa
                tablero.actualizar(escuadron, enemigos);

                if (heroe == heroePrincipal && tablero.esCofre(heroe.getPosicionX(), heroe.getPosicionY())) {
                    cofreAlcanzado = true;
                    System.out.printf("¡%s ha alcanzado el cofre!%n", heroe.getNombre());
                    break;
                }
            }

            // Turno del ente enemigo (IA)
            if (!cofreAlcanzado) {
                turnoEnemigo(escuadron, enemigos);
            }

            // Limpieza de bajas al cierre del turno
            escuadron.removeIf(p -> !p.estaVivo());
            enemigos.removeIf(p -> !p.estaVivo());

        } while (!escuadron.isEmpty() && sumaVida(escuadron) > 0 && !enemigos.isEmpty() && !cofreAlcanzado);

        // ================= Condiciones de victoria / derrota =================
        tablero.actualizar(escuadron, enemigos);
        System.out.printf("%n=============== FIN DE LA BATALLA ===============%n");
        tablero.mostrar();
        if (enemigos.isEmpty()) {
            System.out.printf("¡VICTORIA! Todos los enemigos han sido eliminados.%n");
        } else if (cofreAlcanzado) {
            System.out.printf("¡VICTORIA! %s alcanzó el tesoro.%n", heroePrincipal.getNombre());
        } else {
            System.out.printf("DERROTA. El escuadrón ha caído.%n");
        }
        System.out.printf("Turnos jugados: %d | Héroes en pie: %d%n", turno, escuadron.size());

        scanner.close();
    }

    // Opción 1: el héroe elige un enemigo vivo y le aplica su ataque especial
    private static boolean atacar(Scanner scanner, Personaje heroe, List<Personaje> enemigos) {
        List<Personaje> objetivos = new ArrayList<>();
        for (Personaje e : enemigos) {
            if (e.estaVivo()) {
                objetivos.add(e);
            }
        }

        System.out.printf("Enemigos disponibles:%n");
        for (int i = 0; i < objetivos.size(); i++) {
            Personaje e = objetivos.get(i);
            System.out.printf("  %d. %s | Vida: %d | Posición: (%d,%d)%n",
                    i + 1, e.getNombre(), e.getVida(), e.getPosicionX(), e.getPosicionY());
        }
        System.out.printf("Seleccione el objetivo: ");
        int indice = leerEntero(scanner) - 1;

        // Validación del índice: evita IndexOutOfBoundsException
        if (indice < 0 || indice >= objetivos.size()) {
            System.out.printf("[ERROR] Objetivo inválido.%n");
            return false;
        }

        Personaje objetivo = objetivos.get(indice);
        int vidaAntes = objetivo.getVida();
        // Invocación polimórfica: se ejecuta el ataque especial de la instancia real
        heroe.realizarAtaqueEspecial(objetivo);
        System.out.printf("%s usa su ataque especial contra %s: %d de daño (Vida: %d)%n",
                heroe.getNombre(), objetivo.getNombre(), vidaAntes - objetivo.getVida(), objetivo.getVida());
        if (!objetivo.estaVivo()) {
            System.out.printf("¡%s ha sido derrotado!%n", objetivo.getNombre());
        }
        return true;
    }

    // Opción 2: consume una poción del inventario genérico
    private static boolean usarInventario(Scanner scanner, Personaje heroe, Inventario<Pocion> inventario) {
        if (inventario.estaVacio()) {
            System.out.printf("[ERROR] El inventario está vacío.%n");
            return false;
        }

        System.out.printf("Inventario:%n");
        for (int i = 0; i < inventario.tamanio(); i++) {
            Pocion p = inventario.obtener(i); // sin casteo gracias a los genéricos
            System.out.printf("  %d. %s (+%d vida)%n", i + 1, p.getNombre(), p.getPuntosCuracion());
        }
        System.out.printf("Seleccione la poción: ");
        int indice = leerEntero(scanner) - 1;

        if (indice < 0 || indice >= inventario.tamanio()) {
            System.out.printf("[ERROR] Poción inválida.%n");
            return false;
        }

        Pocion pocion = inventario.remover(indice);
        int curado = heroe.curar(pocion.getPuntosCuracion());
        System.out.printf("%s usa %s y recupera %d de vida (Vida: %d/%d)%n",
                heroe.getNombre(), pocion.getNombre(), curado, heroe.getVida(), heroe.getVidaMaxima());
        return true;
    }

    // Opción 3: mueve al héroe una casilla validando los límites de la matriz
    private static boolean mover(Scanner scanner, Personaje heroe, Tablero tablero) {
        System.out.printf("Dirección (W arriba, S abajo, A izquierda, D derecha): ");
        String direccion = leerTexto(scanner).trim().toUpperCase();

        int nuevaFila = heroe.getPosicionX();
        int nuevaColumna = heroe.getPosicionY();
        switch (direccion) {
            case "W":
                nuevaFila--;
                break;
            case "S":
                nuevaFila++;
                break;
            case "A":
                nuevaColumna--;
                break;
            case "D":
                nuevaColumna++;
                break;
            default:
                System.out.printf("[ERROR] Dirección inválida.%n");
                return false;
        }

        if (!tablero.estaDentro(nuevaFila, nuevaColumna)) {
            System.out.printf("[ERROR] Movimiento fuera del tablero (%d,%d).%n", nuevaFila, nuevaColumna);
            return false;
        } else if (!tablero.esTransitable(nuevaFila, nuevaColumna)) {
            System.out.printf("[ERROR] La celda (%d,%d) está ocupada.%n", nuevaFila, nuevaColumna);
            return false;
        }

        heroe.setPosicion(nuevaFila, nuevaColumna);
        System.out.printf("%s se mueve a (%d,%d)%n", heroe.getNombre(), nuevaFila, nuevaColumna);
        return true;
    }

    // IA enemiga: cada enemigo ataca al héroe vivo con menos vida; en furia usa el ataque sobrecargado
    private static void turnoEnemigo(List<Personaje> escuadron, List<Personaje> enemigos) {
        System.out.printf("%n--- Turno enemigo ---%n");
        for (Personaje enemigo : enemigos) {
            if (!enemigo.estaVivo()) {
                continue;
            }

            Personaje objetivo = null;
            for (Personaje heroe : escuadron) {
                if (heroe.estaVivo() && (objetivo == null || heroe.getVida() < objetivo.getVida())) {
                    objetivo = heroe;
                }
            }
            // Si no queda ningún héroe vivo no hay a quién atacar (evita NullPointerException)
            if (objetivo == null) {
                return;
            }

            int vidaAntes = objetivo.getVida();
            boolean furia = ((Enemigo) enemigo).estaEnFuria();
            enemigo.atacar(objetivo, furia);
            System.out.printf("%s%s ataca a %s: %d de daño (Vida: %d)%n",
                    enemigo.getNombre(), furia ? " (FURIA)" : "", objetivo.getNombre(),
                    vidaAntes - objetivo.getVida(), objetivo.getVida());
            if (!objetivo.estaVivo()) {
                System.out.printf("¡%s ha caído en combate!%n", objetivo.getNombre());
            }
        }
    }

    private static void mostrarEstadisticas(List<Personaje> escuadron, List<Personaje> enemigos, Inventario<Pocion> inventario) {
        System.out.printf("Leyenda: [G] Guerrero [M] Mago [E] Enemigo [C] Cofre [.] Vacía%n");
        System.out.printf("%n%-10s %-9s %-10s %-12s %s%n", "HÉROE", "CLASE", "VIDA", "EXTRA", "POSICIÓN");
        for (Personaje p : escuadron) {
            System.out.printf("%-10s %-9s %3d/%-6d %-12s (%d,%d)%n", p.getNombre(), p.getClass().getSimpleName(),
                    p.getVida(), p.getVidaMaxima(), p.getEstadisticaExtra(), p.getPosicionX(), p.getPosicionY());
        }
        System.out.printf("%n%-10s %-9s %-10s %-12s %s%n", "ENEMIGO", "ATAQUE", "VIDA", "ESTADO", "POSICIÓN");
        for (Personaje e : enemigos) {
            System.out.printf("%-10s %-9d %3d/%-6d %-12s (%d,%d)%n", e.getNombre(), e.getAtaqueBase(),
                    e.getVida(), e.getVidaMaxima(), e.getEstadisticaExtra(), e.getPosicionX(), e.getPosicionY());
        }
        System.out.printf("%nPociones en inventario: %d%n", inventario.tamanio());
    }

    private static boolean hayVivos(List<Personaje> personajes) {
        for (Personaje p : personajes) {
            if (p.estaVivo()) {
                return true;
            }
        }
        return false;
    }

    private static int sumaVida(List<Personaje> personajes) {
        int total = 0;
        for (Personaje p : personajes) {
            total += p.getVida();
        }
        return total;
    }

    // Lee un entero sin romper el programa si el usuario escribe texto
    private static int leerEntero(Scanner scanner) {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            leerTexto(scanner);
            return -1;
        }
    }

    // Lee una palabra; si la entrada de consola se cierra, termina el juego de forma controlada
    private static String leerTexto(Scanner scanner) {
        if (!scanner.hasNext()) {
            System.out.printf("%nEntrada finalizada. Saliendo de la arena...%n");
            System.exit(0);
        }
        return scanner.next();
    }
}
