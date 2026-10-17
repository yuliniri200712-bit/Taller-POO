# Sección 5: Depuración de Encapsulamiento en IntelliJ IDEA

## Caso A: Acceso Directo no Autorizado (Error de Compilador)

```java
Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
heroe.puntosVida = 200;
System.out.println("Vida: " + heroe.puntosVida);
```

**Mensaje del compilador:**
```
error: puntosVida has private access in Personaje
```
En IntelliJ IDEA: `'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'`.

**Causa raíz:** `puntosVida` está declarado `private`, así que solo se puede usar dentro de la clase `Personaje`. `Main` está en otra clase y otro paquete (`co.edu.puj.rpg.principal`), por lo que no puede leerlo ni modificarlo directamente. Si se pudiera, cualquiera podría saltarse la regla de que la vida no supera `vidaMaxima`.

**Corrección:** usar la API pública.
```java
heroe.setPuntosVida(200);                               // el setter lo limita a vidaMaxima (100)
System.out.println("Vida: " + heroe.getPuntosVida());   // imprime 100
```

---

## Caso B: Bypassing de Validaciones mediante Constructor Defectuoso

```java
public Personaje(int puntosVida) {
    this.puntosVida = -50;
    this.estaVivo = true;
}
```

**Resultado:** compila sin errores, pero crea un objeto en un estado inconsistente: tiene vida negativa (`-50`) y aun así aparece como vivo (`true`). Además, ignora el parámetro que recibe.

**Causa raíz:** el constructor asigna los atributos directamente y no pasa por la validación del setter. El encapsulamiento protege los datos desde fuera de la clase, pero dentro de ella el programador también tiene que respetar las reglas de negocio.

**Corrección:** delegar la asignación al setter validado.
```java
public Personaje(int puntosVida) {
    this.vidaMaxima = 100;
    setPuntosVida(puntosVida);   // si es <= 0 → puntosVida = 0 y estaVivo = false
}
```

---

## Caso C: Fuga de Estado y Asignación fuera de Límites

```java
heroe.setPuntosVida(999999); // Excede vidaMaxima
heroe.setPuntosVida(-100);   // Vida negativa sin actualizar estaVivo a false
```

**Resultado:** compila, porque el setter es público. El problema es de diseño: si el setter fuera un simple `this.puntosVida = vida;`, el héroe tendría 999999 de vida (más que su máximo) o -100 de vida mientras sigue vivo. Así el estado se "fuga" de los límites del dominio.

**Causa raíz:** un setter sin validación equivale a hacer el atributo público. Encapsular no es solo poner `private`, sino controlar cada cambio de estado.

**Corrección (implementada en `Personaje.setPuntosVida`):**
```java
public void setPuntosVida(int vida) {
    if (vida <= 0) {
        this.puntosVida = 0;
        this.estaVivo = false;
    } else if (vida > this.vidaMaxima) {
        this.puntosVida = this.vidaMaxima;
        this.estaVivo = true;
    } else {
        this.puntosVida = vida;
        this.estaVivo = true;
    }
}
```
Con esto, `setPuntosVida(999999)` deja la vida en `vidaMaxima`, y `setPuntosVida(-100)` la deja en `0` con `estaVivo = false`.

---

## 6.2. Guía de Verificación en Entorno Integrado

1. **Autocompletado:** al escribir `heroe.` en `Main`, IntelliJ solo muestra los miembros `public`: los getters, `setPuntosVida`, `setOro`, `atacar`, `usarPocion`, `recibirDano`, etc. No aparecen los atributos privados ni `setNivel()`, que es `protected` y `Main` está en otro paquete y no hereda de `Personaje`.
2. **Acceso a miembros privados:** cualquier intento como `heroe.oro` o `heroe.nivel` desde `Main` marca el error `'oro' has private access in 'co.edu.puj.rpg.modelo.Personaje'`, igual que en el Caso A.
3. **Pruebas con valores límite:** la clase `principal/PruebasEncapsulamiento.java` aplica los valores `-10`, `0` y `100000` con los setters:

| Llamada | Vida / Oro resultante | estaVivo / Aceptado |
|---|---|---|
| `setPuntosVida(-10)` | 0 | false |
| `setPuntosVida(0)` | 0 | false |
| `setPuntosVida(100000)` | 100 (vidaMaxima) | true |
| `setOro(-10)` | se mantiene el saldo anterior | false (saldo insuficiente) |
| `setOro(0)` | 0.00 | true |
| `setOro(100000)` | 100000.00 | true |

El objeto mantiene la coherencia de sus atributos en todos los casos.
