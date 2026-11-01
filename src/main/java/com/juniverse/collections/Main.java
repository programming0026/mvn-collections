package com.juniverse.collections;

import com.juniverse.collections.list.ListDemo;
import com.juniverse.collections.map.MapDemo;
import com.juniverse.collections.queue.QueueDemo;
import com.juniverse.collections.set.SetDemo;

import java.util.logging.Logger;
/**
 * Guía práctica de colecciones de Java: qué estructura elegir y por qué, con ejemplos y mediciones.
 */
public final class Main {
    private static final Logger LOGGER = AppLogger.get(Main.class);


    private Main() {
    }

    private static void title(String text) {
        LOGGER.info("\n=========== " + text + " ===========");
    }

    /**
     * Punto de entrada.
     *
     * @param args no se usan
     * @throws InterruptedException si se interrumpe la demostración de colas
     */
    public static void main(String[] args) throws InterruptedException {
        // Salida en UTF-8 para que se vean bien los acentos en cualquier consola
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        title("LISTAS: ArrayList, LinkedList...");
        ListDemo.run();
        title("CONJUNTOS: HashSet, LinkedHashSet, TreeSet, EnumSet");
        SetDemo.run();
        title("MAPAS: HashMap, LinkedHashMap, TreeMap, EnumMap, ConcurrentHashMap");
        MapDemo.run();
        title("COLAS Y PILAS: ArrayDeque, PriorityQueue, BlockingQueue");
        QueueDemo.run();
        LOGGER.info("\nNota: los tiempos son orientativos (calentamiento simple); para medir en serio usa JMH.");
    }
}
