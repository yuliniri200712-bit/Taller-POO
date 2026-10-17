package poo.seccion5;

import java.util.Scanner;

public class SistemaGestionCuentas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Arreglo de tamaño fijo pre-poblado
        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1250.75, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria("1003", "Luis Torres", 300.00, null);

        int opcion;
        do {
            System.out.printf("%n===== SISTEMA DE GESTIÓN DE CUENTAS =====%n");
            System.out.printf("1. Depositar en una cuenta%n");
            System.out.printf("2. Retirar de una cuenta%n");
            System.out.printf("3. Ver reporte consolidado del sistema%n");
            System.out.printf("4. Salir%n");
            System.out.printf("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                scanner.next();
                opcion = 0;
            }

            switch (opcion) {
                case 1: {
                    System.out.printf("Número de cuenta: ");
                    String numero = scanner.next();
                    CuentaBancaria cuenta = buscarCuenta(cuentas, numero);
                    if (cuenta == null) {
                        System.out.printf("[ERROR] La cuenta '%s' no existe.%n", numero);
                    } else {
                        double monto = leerMonto(scanner, "Monto a depositar: ");
                        if (monto <= 0) {
                            System.out.printf("[ERROR] El monto debe ser estrictamente mayor a cero.%n");
                        } else if (cuenta.depositar(monto)) {
                            System.out.printf("Depósito exitoso. Nuevo saldo: %s%n",
                                    ConversorMoneda.formatearMoneda(cuenta.getSaldo()));
                        } else {
                            System.out.printf("[ERROR] No se pudo realizar el depósito.%n");
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.printf("Número de cuenta: ");
                    String numero = scanner.next();
                    CuentaBancaria cuenta = buscarCuenta(cuentas, numero);
                    if (cuenta == null) {
                        System.out.printf("[ERROR] La cuenta '%s' no existe.%n", numero);
                    } else {
                        double monto = leerMonto(scanner, "Monto a retirar: ");
                        if (monto <= 0) {
                            System.out.printf("[ERROR] El monto debe ser estrictamente mayor a cero.%n");
                        } else if (cuenta.retirar(monto)) {
                            System.out.printf("Retiro exitoso. Nuevo saldo: %s%n",
                                    ConversorMoneda.formatearMoneda(cuenta.getSaldo()));
                        } else {
                            System.out.printf("[ERROR] Fondos insuficientes para retirar %s.%n",
                                    ConversorMoneda.formatearMoneda(monto));
                        }
                    }
                    break;
                }
                case 3: {
                    StringBuilder reporte = new StringBuilder();
                    double saldoTotal = 0.0;
                    for (int i = 0; i < cuentas.length; i++) {
                        CuentaBancaria c = cuentas[i];
                        double credito = c.getLimiteCredito() != null ? c.getLimiteCredito() : 0.0;
                        reporte.append(String.format("Cuenta: %s | Titular: %s | Saldo: $%.2f | Crédito: $%.2f%n",
                                c.getNumeroCuenta(), c.getTitular(), c.getSaldo(), credito));
                        saldoTotal += c.getSaldo();
                    }
                    System.out.printf("%n=== REPORTE CONSOLIDADO ===%n%s", reporte);
                    System.out.printf("Total de cuentas: %d | Saldo total acumulado: $%.2f%n", cuentas.length, saldoTotal);
                    break;
                }
                case 4:
                    System.out.printf("Saliendo del sistema...%n");
                    break;
                default:
                    System.out.printf("[ERROR] Opción no válida. Intente de nuevo.%n");
            }
        } while (opcion != 4);

        scanner.close();
    }

    // Busca una cuenta recorriendo el arreglo; retorna null si no existe
    private static CuentaBancaria buscarCuenta(CuentaBancaria[] cuentas, String numeroCuenta) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                return cuentas[i];
            }
        }
        return null;
    }

    private static double leerMonto(Scanner scanner, String mensaje) {
        System.out.printf(mensaje);
        if (scanner.hasNextDouble()) {
            return scanner.nextDouble();
        } else {
            scanner.next();
            return -1;
        }
    }
}
