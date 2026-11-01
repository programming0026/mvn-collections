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

import java.util.logging.Logger;
import com.juniverse.collections.AppLogger;
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
    private static final Logger LOGGER = AppLogger.get(QueueDemo.class);


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
        Deque<String> stack = new ArrayDeque<>();
        stack.push("uno");
        stack.push("dos");
        stack.push("tres");
        LOGGER.info("Pila (LIFO) con ArrayDeque, pop -> " + stack.pop() + ", " + stack.pop());

        Queue<String> queue = new ArrayDeque<>();
        queue.offer("primero");
        queue.offer("segundo");
        LOGGER.info("Cola (FIFO) con ArrayDeque, poll -> " + queue.poll() + ", " + queue.poll());

        PriorityQueue<Task> tasks = new PriorityQueue<>((a, b) -> Integer.compare(a.priority(), b.priority()));
        tasks.add(new Task("enviar reporte", 3));
        tasks.add(new Task("corregir error crítico", 1));
        tasks.add(new Task("actualizar docs", 5));
        StringBuilder order = new StringBuilder();
        while (!tasks.isEmpty()) {
            order.append(tasks.poll().name()).append(" | ");
        }
        LOGGER.info("PriorityQueue (menor número = más urgente): " + order);

        BlockingQueue<Integer> mailbox = new ArrayBlockingQueue<>(2);
        Thread producer = Thread.ofVirtual().start(() -> {
            try {
                for (int i = 1; i <= 4; i++) {
                    mailbox.put(i); // se bloquea si la cola está llena
                }
                mailbox.put(-1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        StringBuilder received = new StringBuilder();
        for (int v = mailbox.take(); v != -1; v = mailbox.take()) {
            received.append(v).append(' ');
        }
        producer.join();
        LOGGER.info("BlockingQueue productor-consumidor recibió: " + received.toString().trim());

        int n = 300_000;
        LOGGER.info("Usar como pila: " + n + " push y pop:");
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
        LOGGER.info("Regla: ArrayDeque para pilas y colas; PriorityQueue si importa la prioridad; nunca Stack.");
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
