package poo.seccion4;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    /*
     * PREGUNTAS DE ANÁLISIS:
     * 1. ¿Qué diferencia ocurre a nivel de memoria y asignación por defecto al
     *    declarar 'saldo' como double primitivo frente a 'limiteCredito' como Wrapper Double?
     *
     *    R: 'saldo' (double) almacena el valor numérico directamente dentro del objeto
     *    CuentaBancaria, ocupa 8 bytes y, como atributo, Java le asigna por defecto 0.0.
     *    'limiteCredito' (Double) es una referencia a un objeto independiente en el heap:
     *    el campo guarda solo la dirección y el objeto Double ocupa memoria adicional
     *    (cabecera + valor). Como atributo de referencia su valor por defecto es null,
     *    es decir, no apunta a ningún objeto hasta que se le asigne uno.
     *
     * 2. ¿Por qué una operación aritmética sobre un atributo no inicializado de tipo
     *    Double provoca una NullPointerException, mientras que sobre un double primitivo
     *    opera con 0.0?
     *
     *    R: Para operar aritméticamente con un Double, Java hace unboxing automático
     *    llamando internamente a limiteCredito.doubleValue(). Si la referencia es null,
     *    no existe objeto sobre el cual invocar ese método y se lanza NullPointerException.
     *    El double primitivo no es una referencia: siempre contiene un valor (0.0 por
     *    defecto), por lo que la operación se realiza normalmente con ese valor.
     */

    // Constructor por defecto
    public CuentaBancaria() {
        this.numeroCuenta = "000000";
        this.titular = "Sin Nombre";
        this.saldo = 0.0;
        this.limiteCredito = 0.0;
    }

    // Constructor de 2 parámetros
    public CuentaBancaria(String numeroCuenta, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0.0;
        this.limiteCredito = 100.0;
    }

    // Constructor completo de 4 parámetros
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

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }
}
