package com.week2;

import java.util.LinkedList;
import java.util.Queue;

public class BasicProducerConsumer {
    private static final int MAX_SIZE = 10;
    private final Queue<Integer> buffer = new LinkedList<>();

    public synchronized void produce(int item) {
        try {
            while (buffer.size() == MAX_SIZE) {
                wait();
            }
            if (item == 5) {
                throw new RuntimeException("Forced exception in produce");
            }
            buffer.add(item);
            System.out.println("Produced: " + item);
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Producer thread interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected exception in producer: " + e.getMessage());
        }
    }

    public synchronized int consume() {
        try {
            while (buffer.isEmpty()) {
                wait();
            }
            int item = buffer.poll();
            if (item == 3) {
                throw new RuntimeException("Forced exception in consume");
            }
            System.out.println("Consumed: " + item);
            notifyAll();
            return item;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Consumer thread interrupted: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.err.println("Unexpected exception in consumer: " + e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BasicProducerConsumer pc = new BasicProducerConsumer();

        Runnable producerTask = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    pc.produce(i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer task interrupted: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected exception in producer task: " + e.getMessage());
            }
        };

        Runnable consumerTask = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    pc.consume();
                    Thread.sleep(15);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Consumer task interrupted: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected exception in consumer task: " + e.getMessage());
            }
        };

        long startTime = System.currentTimeMillis();

        Thread producer = new Thread(producerTask);
        Thread consumer = new Thread(consumerTask);

        producer.start();
        consumer.start();

        try {

            Thread.sleep(100);
            producer.interrupt();

            Thread.sleep(100);
            consumer.interrupt();

            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Main thread interrupted: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Basic Producer-Consumer Time: " + (endTime - startTime) + "ms");
    }
}
