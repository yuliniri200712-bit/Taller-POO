# Sección 6: Análisis y Depuración de Código (Debugging POO)

## Caso de Estudio 1: Excepción de Referencia Nula (`NullPointerException`)

```java
Estudiante estudiante1;
String reporte = estudiante1.obtenerReporteAcademicos();
```

**1. ¿Qué error indica el compilador o qué excepción se lanza?**
Como `estudiante1` es una variable local sin asignar, el compilador no permite compilar y muestra:
`error: variable estudiante1 might not have been initialized`.
Si la variable se inicializara explícitamente en `null` (`Estudiante estudiante1 = null;`), el código compilaría, pero al ejecutar se lanzaría `java.lang.NullPointerException`.

**2. ¿Por qué `estudiante1` no puede invocar el método de instancia?**
Porque solo se declaró la referencia; no se creó ningún objeto `Estudiante` en memoria. Un método de instancia necesita un objeto concreto (con su estado) sobre el cual ejecutarse, y la variable no apunta a ninguno.

**3. Instrucción para instanciar correctamente:**
```java
Estudiante estudiante1 = new Estudiante("2024001", "Laura Martínez", 4.35, true);
```

---

## Caso de Estudio 2: Invocación de Contexto de Instancia desde Contexto Estático

```java
public static void main(String[] args) {
    encenderMotor();
}
```

**1. Mensaje de error del compilador:**
`error: non-static method encenderMotor() cannot be referenced from a static context`

**2. Explicación desde la POO:**
Un método `static` pertenece a la clase, no a un objeto; se ejecuta sin que exista ninguna instancia y por tanto no tiene referencia `this`. Un método de instancia, en cambio, opera sobre el estado de un objeto específico. Desde `main` no hay ningún objeto sobre el cual llamar `encenderMotor()`, así que Java no sabe a qué instancia aplicarlo.

**3. Dos alternativas de corrección:**
- Crear una instancia y llamar el método a través de ella:
  ```java
  GestorVehiculos gestor = new GestorVehiculos();
  gestor.encenderMotor();
  ```
- Declarar el método como estático (si no depende del estado de un objeto):
  ```java
  public static void encenderMotor() { ... }
  ```

---

## Caso de Estudio 3: Firma Incorrecta en la Declaración de un Constructor

```java
public void CuentaBancaria(String numeroCuenta, double saldoInicial) { ... }
```

**1. ¿Por qué `void` hace que deje de ser constructor?**
Un constructor no declara tipo de retorno (ni siquiera `void`). Al escribir `void`, Java lo interpreta como un método de instancia común que casualmente se llama igual que la clase; ya no se ejecuta al usar `new`.

**2. ¿Por qué `new CuentaBancaria("A-101", 500.0)` no compila?**
Como la clase no declara ningún constructor real, el compilador solo genera el constructor por defecto sin parámetros. No existe un constructor que reciba `(String, double)`, por lo que se reporta:
`error: constructor CuentaBancaria in class CuentaBancaria cannot be applied to given types`.

**3. Firma y declaración correcta:**
```java
public CuentaBancaria(String numeroCuenta, double saldoInicial) {
    this.numeroCuenta = numeroCuenta;
    this.saldo = saldoInicial;
}
```

---

## Caso de Estudio 4: Sombreado de Variables (*Variable Shadowing*) por Ausencia de `this`

```java
public Vehiculo(int velocidadMaxima, boolean encendido) {
    velocidadMaxima = velocidadMaxima;
    encendido = encendido;
}
```

**1. ¿Qué se imprime con `new Vehiculo(200, true)` y `v.imprimirEstado()`?**
```
Velocidad Máxima: 0 | Encendido: false
```
Las asignaciones solo copian cada parámetro sobre sí mismo; los atributos nunca se modifican y conservan sus valores por defecto (`0` para `int` y `false` para `boolean`).

**2. Fenómeno de sombreado de variables:**
Cuando un parámetro (o variable local) tiene el mismo nombre que un atributo, dentro de ese bloque el nombre se refiere a la variable más cercana: el parámetro "oculta" (sombrea) al atributo. Por eso `velocidadMaxima = velocidadMaxima` asigna el parámetro a sí mismo y el atributo de la instancia queda intacto.

**3. Constructor corregido con `this`:**
```java
public Vehiculo(int velocidadMaxima, boolean encendido) {
    this.velocidadMaxima = velocidadMaxima;
    this.encendido = encendido;
}
```
