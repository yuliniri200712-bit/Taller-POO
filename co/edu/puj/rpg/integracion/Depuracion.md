# Sección 6: Puntos Críticos de Depuración en IntelliJ IDEA

## 7.1. Trampas de Código y Antipatrones

| Antipatrón | Riesgo | Cómo se evita en este proyecto |
|---|---|---|
| Ocultamiento de excepciones (*Exception Swallowing*) | Un `catch` vacío, o que solo hace `e.printStackTrace()`, oculta el error y el usuario no sabe qué pasó. | Cada `catch` de `Main` muestra `e.getMessage()` con `System.err` y el menú sigue funcionando. No hay ningún `catch` vacío. |
| Pérdida de la causa raíz | Al envolver una excepción en otra sin pasar la original, se pierde el rastro de la pila. | `JuegoException`, `SinManaException`, `CapacidadExcedidaException` y `VidaInsuficienteException` tienen el constructor `(String mensaje, Throwable causa)`, que llama a `super(mensaje, causa)`. |
| Acceso a claves nulas o inexistentes | Suponer que `map.get(clave)` nunca devuelve `null` lleva a una `NullPointerException` al hacer *unboxing*. | `Bestiario.registrarVictoria()` usa `getOrDefault(clave, 0)`. `obtenerDerrotas()` y `CombateEngine.actualizarEstadistica()` revisan `containsKey(clave)` antes de llamar a `get()`. |
| Violación del contrato `equals()` / `hashCode()` | Si se guardan objetos propios en un `HashSet` o `HashMap` sin sobrescribir estos métodos, dos objetos iguales se tratan como distintos y se duplican. | `Item` sobrescribe `equals(Object o)` y `hashCode()` usando el nombre (sin distinguir mayúsculas) y la rareza, así que funciona bien en `Set<Item>` o `Map<Item, ?>`. Los conjuntos actuales usan `String`, que ya cumple el contrato. |

## 7.2. Lista de Comprobación de Depuración (Debugging Checklist)

1. **Puntos de interrupción (*Breakpoints*):**
   - Pon un *breakpoint* en la línea de `Main.asignarHabilidad()` que llama a `personaje.aprenderHabilidad(habilidad)`.
   - Pon un *breakpoint* condicional en la línea `throw new SinManaException(...)` de `CombateEngine.ejecutarAtaqueEspecial()`. Para hacerlo, haz clic derecho sobre el punto rojo y escribe la condición `personaje.getManaActual() < costoMana`.
2. **Inspección de colecciones en tiempo de ejecución:**
   - Inicia `co.edu.puj.rpg.integracion.Main` en modo Debug con **Shift + F9**.
   - En la pestaña **Variables**, despliega el objeto `personaje` y revisa el `HashSet` de `habilidades`. Luego despliega `engine → bestiario → bestiarioMap` (el `HashMap`) y revisa su tabla de *buckets* y su `size`.
3. **Evaluar expresiones al vuelo (*Evaluate Expression*, Alt + F8):** con la ejecución pausada en el ataque especial, prueba:
   - `engine.getBestiario().getBestiarioMap().containsKey("Orcos")`
   - `personaje.getHabilidades().contains("Bola de Fuego")`
   - `personaje.getManaActual() < costoMana`

   Comprueba que los valores coincidan con lo esperado antes de continuar con **F9**.
