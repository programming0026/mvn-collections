# Colecciones de Java: cuándo usar cada una

Guía práctica y ejecutable del Java Collections Framework: **qué estructura elegir y por qué**, con ejemplos de uso y pequeñas mediciones de rendimiento. Java 21, sin dependencias externas.

## Guía rápida

| Necesito... | Usa | Por qué |
|-------------|-----|---------|
| Una lista general | `ArrayList` | Acceso por índice O(1) y recorrido muy rápido |
| Insertar/borrar en los extremos | `ArrayDeque` | Más rápida y compacta que `LinkedList` |
| Insertar/borrar en medio con un iterador | `LinkedList` | O(1) por operación, pero el acceso por índice es O(n); rara vez compensa |
| Una lista que no cambia | `List.of(...)` | Inmutable y segura |
| Una lista leída por muchos hilos | `CopyOnWriteArrayList` | Lecturas sin bloqueo; cada escritura copia todo |
| Elementos sin repetir, lo más rápido | `HashSet` | O(1) |
| Sin repetir y con orden de inserción | `LinkedHashSet` | O(1) y orden estable |
| Sin repetir y ordenado / con rangos | `TreeSet` | O(log n), `first`, `ceiling`, `subSet`... |
| Sin repetir, valores de un `enum` | `EnumSet` | Vector de bits |
| Clave → valor general | `HashMap` | O(1) |
| Clave → valor con orden de inserción o caché LRU | `LinkedHashMap` | Orden predecible y `removeEldestEntry` |
| Clave → valor ordenado por clave / rangos | `TreeMap` | `floorKey`, `headMap`, `subMap`... |
| Clave de un `enum` | `EnumMap` | Compacto y rápido |
| Mapa compartido entre hilos | `ConcurrentHashMap` | Sin bloquear todo el mapa; `merge` atómico |
| Pila o cola de un hilo | `ArrayDeque` | Reemplaza a `Stack` |
| Sacar siempre el más urgente | `PriorityQueue` | Montículo binario |
| Pasar trabajo entre hilos | `ArrayBlockingQueue` | Bloquea al llenarse o vaciarse |

## Qué contiene cada paquete

| Paquete | Clase | Contenido |
|---------|-------|-----------|
| `list` | `ListDemo` | `ArrayList` vs `LinkedList` (acceso por índice, insertar al inicio, borrar con iterador), `List.of` y `CopyOnWriteArrayList` |
| `set` | `SetDemo` | Orden de `HashSet`, `LinkedHashSet` y `TreeSet`, búsquedas de rango y `EnumSet` |
| `map` | `MapDemo` | `TreeMap` con `floorEntry`, caché LRU con `LinkedHashMap`, `EnumMap` y `ConcurrentHashMap.merge` |
| `queue` | `QueueDemo` | Pila y cola con `ArrayDeque`, `PriorityQueue`, productor-consumidor con `BlockingQueue` e hilos virtuales |
| `util` | `Bench` | Medición sencilla de tiempos |

## Requisitos

- JDK 21 y Maven 3.9 o superior.

## Compilar y ejecutar

```bash
mvn compile exec:java

# o como JAR ejecutable (fat jar)
mvn clean package
java -jar target/mvn-collections-1.0.0-SNAPSHOT-launcher.jar
```

## Notas

- Los tiempos que imprime el programa son **orientativos**: usan un calentamiento simple y varían según tu equipo. Para medir con rigor usa [JMH](https://github.com/openjdk/jmh).
- La notación O(...) describe cómo crece el costo con el número de elementos `n`; en la práctica también influyen la memoria y la caché del procesador.

## Licencia

Código de ejemplo de uso libre con fines educativos.
