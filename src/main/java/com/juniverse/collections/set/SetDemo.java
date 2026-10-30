package com.juniverse.collections.set;

import com.juniverse.collections.util.Bench;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

    private enum Day { LUN, MAR, MIE, JUE, VIE, SAB, DOM }

    private SetDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        List<String> datos = List.of("pera", "manzana", "uva", "manzana", "kiwi", "pera", "banano");
        System.out.println("Entrada:       " + datos);
        System.out.println("HashSet:       " + new HashSet<>(datos) + "   (orden no garantizado)");
        System.out.println("LinkedHashSet: " + new LinkedHashSet<>(datos) + "   (orden de inserción)");
        TreeSet<String> arbol = new TreeSet<>(datos);
        System.out.println("TreeSet:       " + arbol + "   (ordenado)");
        System.out.println("  primero=" + arbol.first() + ", último=" + arbol.last()
                + ", menor >= 'l' -> " + arbol.ceiling("l") + ", subconjunto [k, p) -> " + arbol.subSet("k", "p"));

        Set<Day> laborales = EnumSet.range(Day.LUN, Day.VIE);
        System.out.println("EnumSet laborales: " + laborales + ", fin de semana: " + EnumSet.complementOf(EnumSet.copyOf(laborales)));

        int n = 200_000;
        System.out.println("Insertar " + n + " enteros y consultar contains:");
        Bench.row("add + contains", "HashSet", Bench.ms(() -> fill(new HashSet<>(), n)));
        Bench.row("add + contains", "LinkedHashSet", Bench.ms(() -> fill(new LinkedHashSet<>(), n)));
        Bench.row("add + contains", "TreeSet", Bench.ms(() -> fill(new TreeSet<>(), n)));
        System.out.println("Regla: HashSet por defecto; LinkedHashSet si importa el orden; TreeSet si necesitas orden o rangos.");
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
            System.out.println(hits);
        }
    }
}
