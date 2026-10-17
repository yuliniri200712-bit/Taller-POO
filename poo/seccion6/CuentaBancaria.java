package poo.seccion6;

// Caso de Estudio 3 corregido: el constructor no declara tipo de retorno
public class CuentaBancaria {
    private String numeroCuenta;
    private double saldo;

    public CuentaBancaria(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }

    public void imprimirEstado() {
        System.out.printf("Cuenta: %s | Saldo: $%.2f%n", numeroCuenta, saldo);
    }

    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria("A-101", 500.0);
        cuenta.imprimirEstado();
    }
}
