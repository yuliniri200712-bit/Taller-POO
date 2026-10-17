package poo.seccion1;

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
}
