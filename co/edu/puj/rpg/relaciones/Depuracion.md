# 5. Puntos de Depuración y Diagnóstico en IntelliJ IDEA

## 5.1. Detección de Desbordamiento de Pila (`StackOverflowError`)

**Problema:** si `Personaje.toString()` imprime su `Gremio` completo y `Gremio.toString()` imprime el `toString()` completo de cada miembro, las llamadas se repiten sin fin:

```
Personaje.toString() ---> Gremio.toString() ---> Personaje.toString() ---> StackOverflowError
```

**Diagnóstico en IntelliJ IDEA:**
1. Coloca un *breakpoint* en la primera línea de `Personaje.toString()`.
2. Ejecuta `AplicacionArenaRPG` en modo Debug (ícono del insecto o **Shift + F9**).
3. En la ventana **Frames / Call Stack**, si `Personaje.toString()` y `Gremio.toString()` se alternan cientos de veces, se confirma la recursión circular.

**Solución aplicada en el código:** la cadena se rompe en los dos lados de la relación:
- `Gremio.toString()` y `Escuadron.toString()` solo imprimen `getNombre()` de cada miembro.
- `Personaje.toString()` solo imprime `gremio.getNombre()`.

Ningún `toString()` llama al `toString()` completo del otro extremo.

## 5.2. Diagnóstico de Referencias Nulas y Desincronización

| Error / Excepción | Causa raíz en las relaciones | Cómo se previene en este proyecto | Punto de control en IntelliJ IDEA |
|---|---|---|---|
| `NullPointerException` | Llamar a un método sobre una referencia sin instanciar, como `armaEquipada.getDanioBase()` cuando el arma es `null`, o no crear el `CorazonRPG` en el constructor. | `calcularAtaqueTotal()`, `desequiparArma()` y `toString()` revisan `if (this.armaEquipada != null)` antes de usar el arma. El `CorazonRPG` se crea dentro del constructor de `Personaje` y es `final`, así que nunca es `null`. | En la ventana **Variables** del Debug, revisa si `armaEquipada` muestra `null` antes de la línea que falla. |
| Desincronización bidireccional | Actualizar `Personaje.gremio` sin agregar la instancia a `Gremio.miembros`, o al revés. | `unirseAGremio()` asigna `this.gremio` y llama a `nuevoGremio.agregarMiembro(this)`. `agregarMiembro()` agrega al personaje y, si `p.getGremio() != this`, llama a `p.unirseAGremio(this)`. La recursión se detiene porque cada lado revisa primero si ya está sincronizado (`contains` y la comparación de referencias). Al cambiar de gremio, `salirDeGremio()` lo saca del gremio anterior. | Agrega un **Watch** (**Alt + F8**) sobre `personaje.getGremio()` y `gremio.getMiembros()` para comprobar que coinciden. |
| `ConcurrentModificationException` | Agregar o quitar elementos de una `List<Personaje>` mientras se recorre con `for-each`. | Ningún recorrido `for-each` del proyecto modifica la lista que recorre. `getMiembros()` devuelve una vista de solo lectura (`Collections.unmodifiableList`), así que nadie puede modificar la lista interna desde fuera. Si hace falta quitar mientras se recorre, se usa un `Iterator` o se recorre una copia: `for (Personaje p : new ArrayList<>(miembros))`. | Coloca un *breakpoint* condicional dentro del ciclo y revisa el estado de la lista en la pestaña **Variables**. |

## Agregación vs. Composición en el código

| Criterio | Agregación (`Escuadron -> Personaje`) | Composición (`Personaje -> CorazonRPG`) |
|---|---|---|
| Instanciación | Los personajes se crean fuera y se pasan a `reclutarPersonaje(p)`. | `new CorazonRPG(...)` se hace dentro del constructor de `Personaje`. |
| Ciclo de vida | Expulsar a un personaje o descartar el escuadrón no altera al personaje. | El corazón solo existe dentro de su personaje y desaparece con él. |
| Setters | Se pueden agregar y quitar miembros libremente. | No existe `setCorazonRPG()`. El atributo es `private final` y solo se accede a través de `Personaje` (`getSalud()`, `getMana()`, `obtenerEstadoCorazon()`). |
