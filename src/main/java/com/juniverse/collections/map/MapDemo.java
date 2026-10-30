package com.juniverse.collections.map;

import com.juniverse.collections.util.Bench;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mapas (pares clave-valor).
 *
 * <ul>
 *   <li><b>HashMap</b>: el más rápido (O(1)); sin orden. Las claves necesitan {@code equals} y {@code hashCode} correctos.</li>
 *   <li><b>LinkedHashMap</b>: conserva el orden de inserción o de acceso; con {@code removeEldestEntry} sirve como caché LRU.</li>
 *   <li><b>TreeMap</b>: claves ordenadas (O(log n)) y consultas de rango: {@code floorKey}, {@code headMap}...</li>
 *   <li><b>EnumMap</b>: para claves de un {@code enum}; más compacto y rápido que HashMap.</li>
 *   <li><b>ConcurrentHashMap</b>: seguro entre hilos sin bloquear todo el mapa; no admite claves ni valores nulos.</li>
 * </ul>
 */
public final class MapDemo {

    private enum Level { BAJO, MEDIO, ALTO }

    private MapDemo() {
    }

    /** Caché LRU: conserva las últimas {@code capacity} entradas usadas. */
    public static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = 1L;
        private final int capacity;

        /**
         * Crea la caché.
         *
         * @param capacity máximo de entradas
         */
        public LruCache(int capacity) {
            super(16, 0.75f, true); // true = orden de acceso
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    /** Ejecuta la demostración. */
    public static void run() {
        Map<String, Integer> notas = Map.of("Ana", 9, "Luis", 7);
        System.out.println("Map.of es inmutable: " + new TreeMap<>(notas));

        TreeMap<Integer, String> escala = new TreeMap<>();
        escala.put(0, "Reprobado");
        escala.put(60, "Suficiente");
        escala.put(80, "Notable");
        escala.put(90, "Excelente");
        for (int puntos : new int[]{45, 72, 95}) {
            System.out.println("TreeMap.floorEntry(" + puntos + ") -> " + escala.floorEntry(puntos).getValue());
        }

        LruCache<String, String> cache = new LruCache<>(2);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.get("a"); // "a" pasa a ser la más reciente
        cache.put("c", "3"); // expulsa "b"
        System.out.println("Caché LRU (capacidad 2): " + cache.keySet());

        Map<Level, Integer> conteo = new EnumMap<>(Level.class);
        conteo.merge(Level.ALTO, 1, Integer::sum);
        conteo.merge(Level.BAJO, 1, Integer::sum);
        conteo.merge(Level.ALTO, 1, Integer::sum);
        System.out.println("EnumMap (siempre en el orden del enum): " + conteo);

        Map<String, Integer> palabras = new ConcurrentHashMap<>();
        for (String p : "a b a c b a".split(" ")) {
            palabras.merge(p, 1, Integer::sum); // atómico
        }
        System.out.println("ConcurrentHashMap + merge: " + new TreeMap<>(palabras));

        int n = 200_000;
        System.out.println("put + get de " + n + " claves enteras:");
        Bench.row("put + get", "HashMap", Bench.ms(() -> fill(new HashMap<>(), n)));
        Bench.row("put + get", "LinkedHashMap", Bench.ms(() -> fill(new LinkedHashMap<>(), n)));
        Bench.row("put + get", "TreeMap", Bench.ms(() -> fill(new TreeMap<>(), n)));
        Bench.row("put + get", "ConcurrentHashMap", Bench.ms(() -> fill(new ConcurrentHashMap<>(), n)));
        System.out.println("Regla: HashMap por defecto; TreeMap si necesitas orden o rangos; LinkedHashMap para orden o LRU.");
    }

    private static void fill(Map<Integer, Integer> map, int n) {
        for (int i = 0; i < n; i++) {
            map.put((i * 31) % n, i);
        }
        long sum = 0;
        for (int i = 0; i < n; i++) {
            Integer v = map.get(i);
            if (v != null) {
                sum += v;
            }
        }
        if (sum < 0) {
            System.out.println(sum);
        }
    }
}
