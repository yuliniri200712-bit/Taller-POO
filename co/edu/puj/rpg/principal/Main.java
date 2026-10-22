package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Scanner;

public class Main {

    private static final int CURACION_POCION = 30;
    private static final double COSTO_POCION_GRANDE = 20.0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        Enemigo enemigo = new Enemigo("Orco", 80, 12, 120);

        StringBuilder historial = new StringBuilder();
        int ronda = 0;
        boolean huyo = false;

        System.out.printf("=== SIMULADOR DE BATALLA / ARENA DE HÉROES ===%n");
        System.out.printf("%s se enfrenta a %s%n", heroe.getNombre(), enemigo.getNombre());

        do {
            System.out.printf("%n--- MENÚ DE COMBATE ---%n");
            System.out.printf("1. Atacar%n");
            System.out.printf("2. Usar Poción%n");
            System.out.printf("3. Ver Estado%n");
            System.out.printf("4. Huir%n");
            System.out.printf("5. Tienda%n");
            System.out.printf("Seleccione una opción: ");

            int opcion;
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                scanner.next();
                opcion = -1;
            }

            if (opcion < 1 || opcion > 5) {
                System.out.printf("[ERROR] Opción inválida. Ingrese un número entre 1 y 5.%n");
                continue;
            }

            boolean terminaRonda = false;

            switch (opcion) {
                case 1: {
                    ronda++;
                    historial.append(String.format("[Ronda %d]%n", ronda));

                    int vidaEnemigoAntes = enemigo.getPuntosVida();
                    int nivelAntes = heroe.getNivel();
                    heroe.atacar(enemigo);
                    int danoInfligido = vidaEnemigoAntes - enemigo.getPuntosVida();

                    if (heroe.isUltimoAtaqueCritico()) {
                        historial.append(String.format("  ¡GOLPE CRÍTICO! %s inflige %d de daño a %s%n",
                                heroe.getNombre(), danoInfligido, enemigo.getNombre()));
                    } else {
                        historial.append(String.format("  %s inflige %d de daño a %s%n",
                                heroe.getNombre(), danoInfligido, enemigo.getNombre()));
                    }

                    if (!enemigo.isEstaVivo()) {
                        historial.append(String.format("  %s ha sido derrotado. %s gana %d de experiencia%n",
                                enemigo.getNombre(), heroe.getNombre(), enemigo.getExperienciaOtorgada()));
                        if (heroe.getNivel() > nivelAntes) {
                            historial.append(String.format("  ¡%s sube al nivel %d!%n", heroe.getNombre(), heroe.getNivel()));
                        }
                    } else {
                        contraataque(heroe, enemigo, historial);
                    }
                    terminaRonda = true;
                    break;
                }
                case 2: {
                    ronda++;
                    historial.append(String.format("[Ronda %d]%n", ronda));

                    int vidaAntes = heroe.getPuntosVida();
                    if (heroe.getPocionesGrandes() > 0) {
                        heroe.usarPocionGrande(CURACION_POCION);
                        historial.append(String.format("  %s usa una Poción Grande y recupera %d de vida%n",
                                heroe.getNombre(), heroe.getPuntosVida() - vidaAntes));
                    } else {
                        heroe.usarPocion(CURACION_POCION / 3);
                        historial.append(String.format("  %s usa una poción básica y recupera %d de vida%n",
                                heroe.getNombre(), heroe.getPuntosVida() - vidaAntes));
                    }
                    contraataque(heroe, enemigo, historial);
                    terminaRonda = true;
                    break;
                }
                case 3:
                    mostrarEstado(heroe, enemigo);
                    break;
                case 4:
                    huyo = true;
                    historial.append(String.format("  %s huye del combate%n", heroe.getNombre()));
                    terminaRonda = true;
                    break;
                case 5:
                    tienda(scanner, heroe, historial);
                    break;
                default:
                    System.out.printf("[ERROR] Opción inválida.%n");
            }

            if (terminaRonda) {
                System.out.printf("%n=== HISTORIAL DE BATALLA ===%n%s", historial);
            }
        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        System.out.printf("%n=== RESULTADO FINAL ===%n");
        if (huyo) {
            System.out.printf("%s escapó de la arena.%n", heroe.getNombre());
        } else if (heroe.isEstaVivo()) {
            System.out.printf("¡Victoria! %s derrotó a %s.%n", heroe.getNombre(), enemigo.getNombre());
        } else {
            System.out.printf("Derrota. %s ha caído en combate.%n", heroe.getNombre());
        }
        mostrarEstado(heroe, enemigo);

        scanner.close();
    }

    private static void contraataque(Personaje heroe, Enemigo enemigo, StringBuilder historial) {
        int vidaAntes = heroe.getPuntosVida();
        enemigo.atacar(heroe);
        historial.append(String.format("  %s contraataca e inflige %d de daño a %s%n",
                enemigo.getNombre(), vidaAntes - heroe.getPuntosVida(), heroe.getNombre()));
        if (!heroe.isEstaVivo()) {
            historial.append(String.format("  %s ha sido derrotado%n", heroe.getNombre()));
        }
    }

    private static void mostrarEstado(Personaje heroe, Enemigo enemigo) {
        System.out.printf("%n=== ESTADO DEL COMBATE ===%n");
        System.out.printf("Héroe: %s | Nivel: %d | Vida: %d/%d | Ataque: %d | Oro: %.2f | Exp: %d | Pociones: %d | Vivo: %b%n",
                heroe.getNombre(), heroe.getNivel(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                heroe.getPuntosAtaque(), heroe.getOro(), heroe.getExperiencia(), heroe.getPocionesGrandes(),
                heroe.isEstaVivo());
        System.out.printf("Enemigo: %s | Vida: %d/%d | Ataque: %d | Vivo: %b%n",
                enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima(),
                enemigo.getPuntosAtaque(), enemigo.isEstaVivo());
    }

    private static void tienda(Scanner scanner, Personaje heroe, StringBuilder historial) {
        System.out.printf("%n--- TIENDA ---%n");
        System.out.printf("Oro disponible: %.2f%n", heroe.getOro());
        System.out.printf("1. Comprar Poción Grande de Salud (%.2f de oro, cura %d)%n", COSTO_POCION_GRANDE, CURACION_POCION);
        System.out.printf("2. Volver%n");
        System.out.printf("Seleccione una opción: ");

        int opcion;
        if (scanner.hasNextInt()) {
            opcion = scanner.nextInt();
        } else {
            scanner.next();
            opcion = -1;
        }

        switch (opcion) {
            case 1:

                if (heroe.comprarPocionGrande(COSTO_POCION_GRANDE)) {
                    System.out.printf("Compra exitosa. Pociones: %d | Oro restante: %.2f%n",
                            heroe.getPocionesGrandes(), heroe.getOro());
                    historial.append(String.format("  %s compra una Poción Grande de Salud%n", heroe.getNombre()));
                } else {
                    System.out.printf("[ERROR] Saldo insuficiente. La transacción fue rechazada.%n");
                }
                break;
            case 2:
                System.out.printf("Saliendo de la tienda...%n");
                break;
            default:
                System.out.printf("[ERROR] Opción inválida en la tienda.%n");
        }
    }
}
