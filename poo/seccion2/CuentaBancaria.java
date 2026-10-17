package poo.seccion2;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    public CuentaBancaria(String numeroCuenta, String titular, double saldo, Double limiteCredito) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        double disponible = saldo;
        if (limiteCredito != null) {
            disponible += limiteCredito;
        }

        if (monto > 0 && disponible >= monto) {
            saldo -= monto;
            return true;
        } else {
            return false;
        }
    }

    public double getSaldo() {
        return saldo;
    }
}
