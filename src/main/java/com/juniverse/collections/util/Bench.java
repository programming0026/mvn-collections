package com.juniverse.collections.util;

/**
 * Utilidad mínima para comparar tiempos. Es una medición didáctica (con calentamiento), no un benchmark riguroso:
 * para resultados fiables usa JMH.
 */
public final class Bench {

    private Bench() {
    }

    /**
     * Mide cuánto tarda una operación en milisegundos, tras un calentamiento.
     *
     * @param task operación a medir
     * @return milisegundos de la mejor de tres ejecuciones
     */
    public static double ms(Runnable task) {
        task.run(); // calentamiento: deja que la JVM compile el código
        double best = Double.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            long start = System.nanoTime();
            task.run();
            best = Math.min(best, (System.nanoTime() - start) / 1_000_000.0);
        }
        return best;
    }

    /**
     * Imprime una fila de comparación.
     *
     * @param label descripción de la operación
     * @param name nombre de la estructura
     * @param ms tiempo en milisegundos
     */
    public static void row(String label, String name, double ms) {
        System.out.printf("  %-34s %-18s %9.2f ms%n", label, name, ms);
    }
}
