package poo.seccion2;

public class PruebaSeccion2 {
    public static void main(String[] args) {
        Estudiante estudiante = new Estudiante("2024001", "Laura Martínez", 4.35, true);
        System.out.printf("%s%n", estudiante.obtenerReporteAcademicos());

        CuentaBancaria cuenta = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.00);
        System.out.printf("Depósito 250.75: %b%n", cuenta.depositar(250.75));
        System.out.printf("Depósito -10: %b%n", cuenta.depositar(-10));
        System.out.printf("Retiro 1700: %b%n", cuenta.retirar(1700));
        System.out.printf("Retiro 5000: %b%n", cuenta.retirar(5000));
        System.out.printf("Saldo: %s%n", ConversorMoneda.formatearMoneda(cuenta.getSaldo()));

        // Métodos estáticos invocados sin instanciar ConversorMoneda
        System.out.printf("100 USD en EUR: %s%n", ConversorMoneda.formatearMoneda(ConversorMoneda.convertirDolaresAEuros(100, 0.92)));
        System.out.printf("Conversión inválida: %.2f%n", ConversorMoneda.convertirDolaresAEuros(0, 0.92));
    }
}
