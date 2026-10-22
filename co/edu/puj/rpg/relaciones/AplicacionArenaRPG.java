package co.edu.puj.rpg.relaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AplicacionArenaRPG {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        List<Gremio> gremios = new ArrayList<>();
        List<Escuadron> escuadrones = new ArrayList<>();
        List<Personaje> personajes = new ArrayList<>();

        do {
            System.out.println("\n=== MENÚ ARENA DE HÉROES RPG ===");
            System.out.println("1. Crear Personaje");
            System.out.println("2. Crear Gremio");
            System.out.println("3. Reclutar Personaje a Escuadrón");
            System.out.println("4. Equipar Arma a Personaje");
            System.out.println("5. Mostrar Estado Detallado (Personaje y Corazón RPG)");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero(scanner);

            switch (opcion) {
                case 1:
                    crearPersonaje(scanner, personajes);
                    break;
                case 2:
                    crearGremio(scanner, gremios, personajes);
                    break;
                case 3:
                    reclutarEnEscuadron(scanner, escuadrones, personajes);
                    break;
                case 4:
                    equiparArma(scanner, personajes);
                    break;
                case 5:
                    mostrarEstado(personajes, gremios, escuadrones);
                    break;
                case 6:
                    System.out.println("Cerrando la Arena de Héroes...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    private static void crearPersonaje(Scanner scanner, List<Personaje> personajes) {
        System.out.print("Nombre del personaje: ");
        String nombre = leerTexto(scanner);
        if (nombre.isEmpty()) {
            System.out.printf("[ERROR] El nombre no puede estar vacío.%n");
            return;
        }
        System.out.print("Nivel del personaje (1 o más): ");
        int nivel = leerEntero(scanner);
        if (nivel < 1) {
            System.out.printf("[ERROR] El nivel debe ser mayor o igual a 1.%n");
            return;
        }

        Personaje personaje = new Personaje(nombre, nivel);
        personajes.add(personaje);
        System.out.printf("Personaje %s creado (nivel %d, salud %d, maná %d).%n",
                personaje.getNombre(), personaje.getNivel(), personaje.getSalud(), personaje.getMana());
    }

    private static void crearGremio(Scanner scanner, List<Gremio> gremios, List<Personaje> personajes) {
        System.out.print("Nombre del gremio: ");
        String nombre = leerTexto(scanner);
        if (nombre.isEmpty()) {
            System.out.printf("[ERROR] El nombre no puede estar vacío.%n");
            return;
        }

        Gremio gremio = new Gremio(nombre);
        gremios.add(gremio);
        System.out.printf("Gremio %s creado.%n", gremio.getNombre());

        if (personajes.isEmpty()) {
            return;
        }

        int indice;
        do {
            listarPersonajes(personajes);
            System.out.print("Número del personaje a inscribir en el gremio (0 para terminar): ");
            indice = leerEntero(scanner);
            if (indice >= 1 && indice <= personajes.size()) {
                Personaje p = personajes.get(indice - 1);
                Gremio anterior = p.getGremio();
                p.unirseAGremio(gremio);
                if (anterior != null && anterior != gremio) {
                    System.out.printf("%s abandonó el gremio %s.%n", p.getNombre(), anterior.getNombre());
                }
                System.out.printf("%s pertenece ahora al gremio %s. Miembros: %d%n",
                        p.getNombre(), gremio.getNombre(), gremio.getMiembros().size());
            } else if (indice != 0) {
                System.out.printf("[ERROR] Personaje inválido.%n");
            }
        } while (indice != 0);
    }

    private static void reclutarEnEscuadron(Scanner scanner, List<Escuadron> escuadrones, List<Personaje> personajes) {
        Personaje personaje = seleccionarPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }

        System.out.printf("Escuadrones disponibles:%n");
        for (int i = 0; i < escuadrones.size(); i++) {
            System.out.printf("  %d. %s (%d miembros)%n", i + 1, escuadrones.get(i).getNombre(),
                    escuadrones.get(i).getMiembros().size());
        }
        System.out.printf("  0. Crear un nuevo escuadrón%n");
        System.out.print("Seleccione el escuadrón: ");
        int indice = leerEntero(scanner);

        Escuadron escuadron;
        if (indice == 0) {
            System.out.print("Nombre del nuevo escuadrón: ");
            String nombre = leerTexto(scanner);
            if (nombre.isEmpty()) {
                System.out.printf("[ERROR] El nombre no puede estar vacío.%n");
                return;
            }
            escuadron = new Escuadron(nombre);
            escuadrones.add(escuadron);
        } else if (indice >= 1 && indice <= escuadrones.size()) {
            escuadron = escuadrones.get(indice - 1);
        } else {
            System.out.printf("[ERROR] Escuadrón inválido.%n");
            return;
        }

        if (escuadron.contiene(personaje)) {
            System.out.printf("%s ya forma parte del escuadrón %s.%n", personaje.getNombre(), escuadron.getNombre());
            return;
        }
        escuadron.reclutarPersonaje(personaje);
        System.out.printf("%s fue reclutado en el escuadrón %s. Miembros: %d%n",
                personaje.getNombre(), escuadron.getNombre(), escuadron.getMiembros().size());
    }

    private static void equiparArma(Scanner scanner, List<Personaje> personajes) {
        Personaje personaje = seleccionarPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }

        System.out.print("Nombre del arma: ");
        String nombre = leerTexto(scanner);
        if (nombre.isEmpty()) {
            System.out.printf("[ERROR] El nombre no puede estar vacío.%n");
            return;
        }
        System.out.print("Daño base del arma: ");
        int danio = leerEntero(scanner);
        if (danio < 0) {
            System.out.printf("[ERROR] El daño base no puede ser negativo.%n");
            return;
        }

        if (personaje.getArmaEquipada() != null) {
            personaje.desequiparArma();
        }
        personaje.equiparArma(new Arma(nombre, danio));
        System.out.printf("Ataque total de %s: %d%n", personaje.getNombre(), personaje.calcularAtaqueTotal());
    }

    private static void mostrarEstado(List<Personaje> personajes, List<Gremio> gremios, List<Escuadron> escuadrones) {
        if (personajes.isEmpty()) {
            System.out.printf("No hay personajes registrados.%n");
            return;
        }

        System.out.printf("%n%-12s %-6s %-8s %-8s %-15s %-8s %-15s %-15s%n",
                "PERSONAJE", "NIVEL", "SALUD", "MANÁ", "ARMA", "ATAQUE", "GREMIO", "ESCUADRÓN");
        for (Personaje p : personajes) {
            String arma = p.getArmaEquipada() != null ? p.getArmaEquipada().getNombre() : "Desarmado";
            String gremio = p.getGremio() != null ? p.getGremio().getNombre() : "Sin gremio";
            System.out.printf("%-12s %-6d %-8d %-8d %-15s %-8d %-15s %-15s%n",
                    p.getNombre(), p.getNivel(), p.getSalud(), p.getMana(), arma,
                    p.calcularAtaqueTotal(), gremio, buscarEscuadrones(p, escuadrones));
        }

        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("%n=== REPORTE DETALLADO ===%n"));
        for (Personaje p : personajes) {
            reporte.append(p.getNombre()).append(String.format("%n"));
            reporte.append("    ").append(p.obtenerEstadoCorazon()).append(String.format("%n"));
            if (p.getArmaEquipada() != null) {
                reporte.append("    ").append(p.getArmaEquipada()).append(String.format("%n"));
            }
        }
        reporte.append(String.format("%n=== GREMIOS (%d) ===%n", gremios.size()));
        for (Gremio g : gremios) {
            reporte.append(g).append(String.format("%n"));
        }
        reporte.append(String.format("%n=== ESCUADRONES (%d) ===%n", escuadrones.size()));
        for (Escuadron e : escuadrones) {
            reporte.append(e).append(String.format("%n"));
        }
        System.out.printf("%s", reporte);
    }

    private static String buscarEscuadrones(Personaje p, List<Escuadron> escuadrones) {
        StringBuilder sb = new StringBuilder();
        for (Escuadron e : escuadrones) {
            if (e.contiene(p)) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(e.getNombre());
            }
        }
        return sb.length() > 0 ? sb.toString() : "Ninguno";
    }

    private static Personaje seleccionarPersonaje(Scanner scanner, List<Personaje> personajes) {
        if (personajes.isEmpty()) {
            System.out.printf("[ERROR] Primero debe crear un personaje.%n");
            return null;
        }
        listarPersonajes(personajes);
        System.out.print("Seleccione el personaje: ");
        int indice = leerEntero(scanner);
        if (indice < 1 || indice > personajes.size()) {
            System.out.printf("[ERROR] Personaje inválido.%n");
            return null;
        }
        return personajes.get(indice - 1);
    }

    private static void listarPersonajes(List<Personaje> personajes) {
        System.out.printf("Personajes registrados:%n");
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            System.out.printf("  %d. %s (nivel %d)%n", i + 1, p.getNombre(), p.getNivel());
        }
    }

    private static int leerEntero(Scanner scanner) {
        String linea = leerTexto(scanner);
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String leerTexto(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            System.out.printf("%nEntrada finalizada. Cerrando la Arena de Héroes...%n");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }
}
