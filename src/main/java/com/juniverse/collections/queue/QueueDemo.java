package com.juniverse.collections.queue;

import com.juniverse.collections.util.Bench;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Colas y pilas.
 *
 * <ul>
 *   <li><b>ArrayDeque</b>: la mejor opción para pilas (LIFO) y colas (FIFO) de un solo hilo; reemplaza a {@code Stack} y suele ganar a {@code LinkedList}.</li>
 *   <li><b>PriorityQueue</b>: extrae siempre el elemento de mayor prioridad (el menor según el orden); para planificadores y algoritmos como Dijkstra.</li>
 *   <li><b>ArrayBlockingQueue</b>: cola con capacidad fija y bloqueo, para pasar trabajo entre hilos (productor-consumidor).</li>
 * </ul>
 */
public final class QueueDemo {

    private record Task(String name, int priority) {
    }

    private QueueDemo() {
    }

    /**
     * Ejecuta la demostración.
     *
     * @throws InterruptedException si se interrumpe la espera del consumidor
     */
    public static void run() throws InterruptedException {
        Deque<String> pila = new ArrayDeque<>();
        pila.push("uno");
        pila.push("dos");
        pila.push("tres");
        System.out.println("Pila (LIFO) con ArrayDeque, pop -> " + pila.pop() + ", " + pila.pop());

        Queue<String> cola = new ArrayDeque<>();
        cola.offer("primero");
        cola.offer("segundo");
        System.out.println("Cola (FIFO) con ArrayDeque, poll -> " + cola.poll() + ", " + cola.poll());

        PriorityQueue<Task> tareas = new PriorityQueue<>((a, b) -> Integer.compare(a.priority(), b.priority()));
        tareas.add(new Task("enviar reporte", 3));
        tareas.add(new Task("corregir error crítico", 1));
        tareas.add(new Task("actualizar docs", 5));
        StringBuilder orden = new StringBuilder();
        while (!tareas.isEmpty()) {
            orden.append(tareas.poll().name()).append(" | ");
        }
        System.out.println("PriorityQueue (menor número = más urgente): " + orden);

        BlockingQueue<Integer> buzon = new ArrayBlockingQueue<>(2);
        Thread productor = Thread.ofVirtual().start(() -> {
            try {
                for (int i = 1; i <= 4; i++) {
                    buzon.put(i); // se bloquea si la cola está llena
                }
                buzon.put(-1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        StringBuilder recibidos = new StringBuilder();
        for (int v = buzon.take(); v != -1; v = buzon.take()) {
            recibidos.append(v).append(' ');
        }
        productor.join();
        System.out.println("BlockingQueue productor-consumidor recibió: " + recibidos.toString().trim());

        int n = 300_000;
        System.out.println("Usar como pila: " + n + " push y pop:");
        Bench.row("push + pop", "ArrayDeque", Bench.ms(() -> stack(new ArrayDeque<>(), n)));
        Bench.row("push + pop", "LinkedList", Bench.ms(() -> stack(new LinkedList<>(), n)));
        Bench.row("push + pop", "Stack (obsoleta)", Bench.ms(() -> {
            Stack<Integer> s = new Stack<>();
            for (int i = 0; i < n; i++) {
                s.push(i);
            }
            while (!s.isEmpty()) {
                s.pop();
            }
        }));
        System.out.println("Regla: ArrayDeque para pilas y colas; PriorityQueue si importa la prioridad; nunca Stack.");
    }

    private static void stack(Deque<Integer> deque, int n) {
        for (int i = 0; i < n; i++) {
            deque.push(i);
        }
        while (!deque.isEmpty()) {
            deque.pop();
        }
    }
}
