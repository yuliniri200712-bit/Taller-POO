# Sección 5: Puntos de Depuración e Inspección en IntelliJ IDEA

## 5.1 Matriz de Excepciones y Errores Comunes

| Excepción / Error | Causa raíz en el código | Cómo se previene en este proyecto |
|---|---|---|
| `IndexOutOfBoundsException` | Acceder a un índice que no existe en el `ArrayList`, o salir de los límites de `tablero[5][5]` al mover un personaje (por ejemplo, índice `5`). | `Tablero.estaDentro(fila, columna)` revisa `0 <= índice < 5` antes de cada movimiento. En `Main.atacar()` y `Main.usarInventario()` se valida `indice >= 0 && indice < lista.size()` antes de llamar a `get()` o `remover()`. |
| `ClassCastException` / errores de tipado | Usar tipos crudos (*raw types*) o castear un `Personaje` a `Mago` cuando la instancia real es un `Guerrero`. | Todas las colecciones usan genéricos explícitos (`List<Personaje>`, `Inventario<Pocion>`), así que `inventario.obtener(i)` ya retorna `Pocion` sin casteo. El único casteo, `(Enemigo) enemigo`, se hace sobre la lista que solo contiene objetos `Enemigo`. |
| Error de compilación: instanciación directa | `Personaje p = new Personaje();` no compila porque `Personaje` es `abstract` (*"Personaje is abstract; cannot be instantiated"*). | Solo se instancian subclases concretas: `new Guerrero(...)`, `new Mago(...)` y `new Enemigo(...)`. |
| `NullPointerException` | Leer una celda o un objetivo que no existe. | `Tablero.limpiar()` llena toda la matriz con `'.'`. En `turnoEnemigo()` se comprueba `objetivo == null` antes de atacar. |

## 5.2 Guía Práctica de Depuración (Debugging)

1. **Puntos de interrupción (*Breakpoints*):** haz clic en el margen izquierdo, junto al número de línea, en estos dos lugares:
   - la primera línea de `realizarAtaqueEspecial()` en `Guerrero.java` y en `Mago.java`;
   - la línea `turno++;` al inicio del `do-while` en `co/edu/puj/rpg/arena/principal/Main.java`.
2. **Modo Debug:** ejecuta `Main` con **Shift + F9** o con el ícono del escarabajo verde.
3. **Paso a paso:**
   - **F8 (*Step Over*)** avanza línea por línea sin entrar en los métodos.
   - **F7 (*Step Into*)** entra en el método. Por ejemplo, entra en `defender()` para ver cómo el escudo del Guerrero (`danoRecibido - fuerzaEscudo`) o la barrera del Mago (`danoRecibido / 2`) reducen el daño.
4. **Panel de variables (*Variables View*):** despliega `escuadron` para ver cada elemento (`Guerrero` o `Mago`) con sus atributos heredados (`nombre`, `vida`, `posicionX`, `posicionY`) y los propios (`fuerzaEscudo`, `puntosMana`).
5. **Evaluar expresiones (Alt + F8):** algunas expresiones útiles:
   - `escuadron.get(0).estaVivo()`
   - `tablero.getMatriz()[escuadron.get(0).getPosicionX()][escuadron.get(0).getPosicionY()]`
   - `enemigos.size()`
   - `inventario.tamanio()`
