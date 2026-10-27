package co.edu.puj.rpg.integracion;

import co.edu.puj.rpg.integracion.engine.CombateEngine;
import co.edu.puj.rpg.integracion.exception.CapacidadExcedidaException;
import co.edu.puj.rpg.integracion.exception.SinManaException;
import co.edu.puj.rpg.integracion.exception.VidaInsuficienteException;
import co.edu.puj.rpg.integracion.model.Item;
import co.edu.puj.rpg.integracion.model.PerfilJugador;
import co.edu.puj.rpg.integracion.model.Personaje;
import co.edu.puj.rpg.integracion.model.enums.RaridadItem;
import co.edu.puj.rpg.integracion.model.enums.TipoPersonaje;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Personaje> personajes = new ArrayList<>();

        System.out.print("Nombre del jugador: ");
        String nombreJugador = leerTexto(scanner);
        PerfilJugador perfil = new PerfilJugador(nombreJugador.isEmpty() ? "Jugador" : nombreJugador);
        CombateEngine engine = new CombateEngine(perfil);

        int opcion;
        do {
            System.out.printf("%n=== ARENA DE HÉROES RPG | Jugador: %s ===%n", perfil.getNombreJugador());
            System.out.println("1. Crear Personaje");
            System.out.println("2. Asignar Habilidad Única");
            System.out.println("3. Registrar Victoria en Bestiario");
            System.out.println("4. Ejecutar Ataque Especial");
            System.out.println("5. Consultar Bestiario y Logros");
            System.out.println("6. Salir");
            opcion = leerEntero(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    crearPersonaje(scanner, personajes);
                    break;
                case 2:
                    asignarHabilidad(scanner, personajes);
                    break;
                case 3:
                    registrarVictoria(scanner, engine);
                    break;
                case 4:
                    ejecutarAtaqueEspecial(scanner, personajes, engine);
                    break;
                case 5:
                    consultarBestiarioYLogros(engine, personajes);
                    break;
                case 6:
                    System.out.println("Cerrando la Arena de Héroes...");
                    break;
                default:
                    System.err.println("[ERROR] Opción inválida. Seleccione un número entre 1 y 6.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    private static void crearPersonaje(Scanner scanner, List<Personaje> personajes) {
        System.out.print("Nombre del personaje: ");
        String nombre = leerTexto(scanner);
        if (nombre.isEmpty()) {
            System.err.println("[ERROR] El nombre no puede estar vacío.");
            return;
        }

        TipoPersonaje[] tipos = TipoPersonaje.values();
        System.out.println("Tipos de personaje disponibles:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.printf("  %d. %-9s | Bonif. ataque: %.1f | Bonif. defensa: %.1f%n", i + 1, tipos[i],
                    tipos[i].getBonificadorAtaqueBase(), tipos[i].getBonificadorDefensaBase());
        }
        int indice = leerEntero(scanner, "Seleccione el tipo: ");
        if (indice < 1 || indice > tipos.length) {
            System.err.println("[ERROR] Tipo de personaje inválido.");
            return;
        }

        Personaje personaje = new Personaje(nombre, tipos[indice - 1]);
        try {
            personaje.agregarItem(itemInicial(personaje.getTipoPersonaje()));
        } catch (CapacidadExcedidaException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        personajes.add(personaje);
        System.out.printf("Personaje %s (%s) creado en estado %s. Ítem inicial: %s%n",
                personaje.getNombre(), personaje.getTipoPersonaje(), personaje.getEstado(),
                personaje.getInventario().get(0));
    }

    private static Item itemInicial(TipoPersonaje tipo) {
        switch (tipo) {
            case GUERRERO:
                return new Item("Espada de Hierro", RaridadItem.COMUN, 4.0);
            case MAGO:
                return new Item("Báculo Arcano", RaridadItem.RARO, 4.0);
            default:
                return new Item("Arco Élfico", RaridadItem.LEGENDARIO, 2.0);
        }
    }

    private static void asignarHabilidad(Scanner scanner, List<Personaje> personajes) {
        Personaje personaje = seleccionarPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }
        System.out.print("Nombre de la habilidad: ");
        String habilidad = leerTexto(scanner);
        try {
            if (personaje.aprenderHabilidad(habilidad)) {
                System.out.printf("%s aprendió la habilidad '%s'. Habilidades: %d/%d%n", personaje.getNombre(),
                        habilidad, personaje.obtenerHabilidades().size(), Personaje.CAPACIDAD_MAXIMA_HABILIDADES);
            } else {
                System.out.printf("Advertencia: %s ya tiene aprendida la habilidad '%s' (o el nombre es inválido). "
                        + "No se agregó.%n", personaje.getNombre(), habilidad);
            }
        } catch (CapacidadExcedidaException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    private static void registrarVictoria(Scanner scanner, CombateEngine engine) {
        System.out.print("Nombre del enemigo derrotado: ");
        String enemigo = leerTexto(scanner);
        try {
            boolean primera = engine.registrarVictoria(enemigo);
            System.out.printf("Victoria registrada contra %s. Total de derrotas de este enemigo: %d%n",
                    enemigo, engine.getBestiario().obtenerDerrotas(enemigo));
            if (primera) {
                System.out.printf("¡Logro desbloqueado: Primera victoria contra %s!%n", enemigo);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    private static void ejecutarAtaqueEspecial(Scanner scanner, List<Personaje> personajes, CombateEngine engine) {
        Personaje personaje = seleccionarPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }
        int costoMana = leerEntero(scanner, "Costo de maná del ataque: ");
        int costoVida = leerEntero(scanner, "Costo de vida del ataque (0 si no aplica): ");
        if (costoMana < 0 || costoVida < 0) {
            System.err.println("[ERROR] Los costos no pueden ser negativos.");
            return;
        }
        try {
            System.out.print(engine.ejecutarAtaqueEspecial(personaje, costoMana, costoVida));
        } catch (SinManaException e) {
            System.err.println("[ERROR] " + e.getMessage());
        } catch (VidaInsuficienteException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    private static void consultarBestiarioYLogros(CombateEngine engine, List<Personaje> personajes) {
        Map<String, Integer> bestiario = engine.getBestiario().getBestiarioMap();
        System.out.printf("%n=== BESTIARIO (%d tipos de enemigo) ===%n", bestiario.size());
        if (bestiario.isEmpty()) {
            System.out.println("Aún no hay enemigos derrotados.");
        }
        for (Map.Entry<String, Integer> entry : bestiario.entrySet()) {
            System.out.printf("  %-20s : %3d derrota(s)%n", entry.getKey(), entry.getValue());
        }

        System.out.printf("%n=== LOGROS DE %s (orden alfabético) ===%n", engine.getPerfil().getNombreJugador());
        if (engine.getPerfil().listarLogrosOrdenados().isEmpty()) {
            System.out.println("Aún no hay logros desbloqueados.");
        }
        for (String logro : engine.getPerfil().listarLogrosOrdenados()) {
            System.out.printf("  * %s%n", logro);
        }

        System.out.printf("%n=== ESTADÍSTICAS DE COMBATE ===%n");
        for (Map.Entry<String, Double> entry : engine.getEstadisticas().entrySet()) {
            System.out.printf("  %-35s : %10.2f%n", entry.getKey(), entry.getValue());
        }

        if (!personajes.isEmpty()) {
            System.out.printf("%n%-12s %-9s %-11s %-9s %-9s %-8s %s%n",
                    "PERSONAJE", "TIPO", "ESTADO", "VIDA", "MANÁ", "ATAQUE", "HABILIDADES");
            for (Personaje p : personajes) {
                System.out.printf("%-12s %-9s %-11s %3d/%-5d %3d/%-5d %-8.2f %s%n", p.getNombre(),
                        p.getTipoPersonaje(), p.getEstado(), p.getVidaActual(), p.getVidaMaxima(),
                        p.getManaActual(), p.getManaMaximo(), p.calcularAtaque(), p.obtenerHabilidades());
            }
        }
    }

    private static Personaje seleccionarPersonaje(Scanner scanner, List<Personaje> personajes) {
        if (personajes.isEmpty()) {
            System.err.println("[ERROR] Primero debe crear un personaje.");
            return null;
        }
        System.out.println("Personajes disponibles:");
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            System.out.printf("  %d. %s (%s) | %s | Vida: %d | Maná: %d%n", i + 1, p.getNombre(),
                    p.getTipoPersonaje(), p.getEstado(), p.getVidaActual(), p.getManaActual());
        }
        int indice = leerEntero(scanner, "Seleccione el personaje: ");
        if (indice < 1 || indice > personajes.size()) {
            System.err.println("[ERROR] Personaje inválido.");
            return null;
        }
        return personajes.get(indice - 1);
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("[ERROR] Debe ingresar un número entero. Intente de nuevo.");
            } catch (NoSuchElementException e) {
                System.out.println();
                System.out.println("Entrada finalizada. Cerrando la Arena de Héroes...");
                System.exit(0);
            } finally {
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
        }
    }

    private static String leerTexto(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("Entrada finalizada. Cerrando la Arena de Héroes...");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }
}
