package com.juniverse.collections.set;

import com.juniverse.collections.util.Bench;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import java.util.logging.Logger;
import com.juniverse.collections.AppLogger;
/**
 * Conjuntos (sin elementos repetidos).
 *
 * <ul>
 *   <li><b>HashSet</b>: el más rápido (O(1)); sin orden garantizado.</li>
 *   <li><b>LinkedHashSet</b>: igual de rápido y conserva el orden de inserción.</li>
 *   <li><b>TreeSet</b>: mantiene los elementos ordenados (O(log n)) y ofrece búsquedas como "el menor mayor que x".</li>
 *   <li><b>EnumSet</b>: para valores de un {@code enum}; es un vector de bits, muy compacto y veloz.</li>
 * </ul>
 */
public final class SetDemo {
    private static final Logger LOGGER = AppLogger.get(SetDemo.class);


    private enum Day { MON, MAR, MIE, THU, FRI, SAT, DOM }

    private SetDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        List<String> data = List.of("pera", "manzana", "uva", "manzana", "kiwi", "pera", "banano");
        LOGGER.info("Entrada:       " + data);
        LOGGER.info("HashSet:       " + new HashSet<>(data) + "   (orden no garantizado)");
        LOGGER.info("LinkedHashSet: " + new LinkedHashSet<>(data) + "   (orden de inserción)");
        TreeSet<String> tree = new TreeSet<>(data);
        LOGGER.info("TreeSet:       " + tree + "   (ordenado)");
        LOGGER.info("  primero=" + tree.first() + ", último=" + tree.last()
                + ", menor >= 'l' -> " + tree.ceiling("l") + ", subconjunto [k, p) -> " + tree.subSet("k", "p"));

        Set<Day> workdays = EnumSet.range(Day.MON, Day.FRI);
        LOGGER.info("EnumSet laborales: " + workdays + ", fin de semana: " + EnumSet.complementOf(EnumSet.copyOf(workdays)));

        int n = 200_000;
        LOGGER.info("Insertar " + n + " enteros y consultar contains:");
        Bench.row("add + contains", "HashSet", Bench.ms(() -> fill(new HashSet<>(), n)));
        Bench.row("add + contains", "LinkedHashSet", Bench.ms(() -> fill(new LinkedHashSet<>(), n)));
        Bench.row("add + contains", "TreeSet", Bench.ms(() -> fill(new TreeSet<>(), n)));
        LOGGER.info("Regla: HashSet por defecto; LinkedHashSet si importa el orden; TreeSet si necesitas orden o rangos.");
    }

    private static void fill(Set<Integer> set, int n) {
        for (int i = 0; i < n; i++) {
            set.add((i * 31) % n);
        }
        int hits = 0;
        for (int i = 0; i < n; i++) {
            if (set.contains(i)) {
                hits++;
            }
        }
        if (hits < 0) {
            LOGGER.info(String.valueOf(hits));
        }
    }
}
