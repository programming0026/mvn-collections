package com.juniverse.collections.map;

import com.juniverse.collections.util.Bench;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import java.util.logging.Logger;
import com.juniverse.collections.AppLogger;
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
    private static final Logger LOGGER = AppLogger.get(MapDemo.class);


    private enum Level { LOW, MEDIUM, HIGH }

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
        Map<String, Integer> grades = Map.of("Ana", 9, "Luis", 7);
        LOGGER.info("Map.of es inmutable: " + new TreeMap<>(grades));

        TreeMap<Integer, String> scale = new TreeMap<>();
        scale.put(0, "Reprobado");
        scale.put(60, "Suficiente");
        scale.put(80, "Notable");
        scale.put(90, "Excelente");
        for (int points : new int[]{45, 72, 95}) {
            LOGGER.info("TreeMap.floorEntry(" + points + ") -> " + scale.floorEntry(points).getValue());
        }

        LruCache<String, String> cache = new LruCache<>(2);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.get("a"); // "a" pasa a ser la más reciente
        cache.put("c", "3"); // expulsa "b"
        LOGGER.info("Caché LRU (capacidad 2): " + cache.keySet());

        Map<Level, Integer> count = new EnumMap<>(Level.class);
        count.merge(Level.HIGH, 1, Integer::sum);
        count.merge(Level.LOW, 1, Integer::sum);
        count.merge(Level.HIGH, 1, Integer::sum);
        LOGGER.info("EnumMap (siempre en el orden del enum): " + count);

        Map<String, Integer> words = new ConcurrentHashMap<>();
        for (String p : "a b a c b a".split(" ")) {
            words.merge(p, 1, Integer::sum); // atómico
        }
        LOGGER.info("ConcurrentHashMap + merge: " + new TreeMap<>(words));

        int n = 200_000;
        LOGGER.info("put + get de " + n + " claves enteras:");
        Bench.row("put + get", "HashMap", Bench.ms(() -> fill(new HashMap<>(), n)));
        Bench.row("put + get", "LinkedHashMap", Bench.ms(() -> fill(new LinkedHashMap<>(), n)));
        Bench.row("put + get", "TreeMap", Bench.ms(() -> fill(new TreeMap<>(), n)));
        Bench.row("put + get", "ConcurrentHashMap", Bench.ms(() -> fill(new ConcurrentHashMap<>(), n)));
        LOGGER.info("Regla: HashMap por defecto; TreeMap si necesitas orden o rangos; LinkedHashMap para orden o LRU.");
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
            LOGGER.info(String.valueOf(sum));
        }
    }
}
