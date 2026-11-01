package com.juniverse.collections.list;

import com.juniverse.collections.util.Bench;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.logging.Logger;
import com.juniverse.collections.AppLogger;
/**
 * Listas: {@code ArrayList} vs {@code LinkedList} (y las variantes inmutable y concurrente).
 *
 * <ul>
 *   <li><b>ArrayList</b>: arreglo dinámico. Acceso por índice O(1); insertar/borrar en medio o al inicio O(n). Es la opción por defecto.</li>
 *   <li><b>LinkedList</b>: nodos enlazados. Insertar/borrar con un iterador ya posicionado es O(1), pero el acceso por índice es O(n)
 *       y consume más memoria. Rara vez gana en la práctica.</li>
 *   <li><b>List.of(...)</b>: inmutable, ideal para constantes y para devolver datos que no deben modificarse.</li>
 *   <li><b>CopyOnWriteArrayList</b>: seguro entre hilos; cada escritura copia el arreglo, así que solo conviene con muchas lecturas y pocas escrituras.</li>
 * </ul>
 */
public final class ListDemo {
    private static final Logger LOGGER = AppLogger.get(ListDemo.class);


    private static final int N = 30_000;

    private ListDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        List<Integer> array = new ArrayList<>();
        List<Integer> linked = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            array.add(i);
            linked.add(i);
        }

        LOGGER.info("Acceso aleatorio por índice (get):");
        Bench.row("get(i) en " + N + " elementos", "ArrayList", Bench.ms(() -> sumByIndex(array)));
        Bench.row("get(i) en " + N + " elementos", "LinkedList", Bench.ms(() -> sumByIndex(linked)));

        LOGGER.info("Insertar siempre al inicio (add(0, x)):");
        Bench.row("add(0, x) x " + N, "ArrayList", Bench.ms(() -> insertAtStart(new ArrayList<>())));
        Bench.row("add(0, x) x " + N, "LinkedList", Bench.ms(() -> insertAtStart(new LinkedList<>())));

        LOGGER.info("Borrar elementos mientras se recorre (ListIterator.remove):");
        Bench.row("borrar los pares", "ArrayList", Bench.ms(() -> removeEvens(new ArrayList<>(array))));
        Bench.row("borrar los pares", "LinkedList", Bench.ms(() -> removeEvens(new LinkedList<>(linked))));

        List<String> fixed = List.of("A", "B", "C");
        try {
            fixed.add("D");
        } catch (UnsupportedOperationException e) {
            LOGGER.info("List.of es inmutable: add lanza " + e.getClass().getSimpleName());
        }
        List<String> concurrent = new CopyOnWriteArrayList<>(fixed);
        for (String s : concurrent) {
            concurrent.add(s.toLowerCase()); // no lanza ConcurrentModificationException
        }
        LOGGER.info("CopyOnWriteArrayList permite modificar mientras se recorre: " + concurrent);
        LOGGER.info("Regla: usa ArrayList salvo que tengas un motivo medido para otra cosa.");
    }

    private static long sumByIndex(List<Integer> list) {
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }

    private static void insertAtStart(List<Integer> list) {
        for (int i = 0; i < N; i++) {
            list.add(0, i);
        }
    }

    private static void removeEvens(List<Integer> list) {
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) {
                it.remove();
            }
        }
    }
}
