package com.week2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueProducerConsumer {
    private static final int CAPACITY = 10;
    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(CAPACITY);

    public void produce(int item) throws InterruptedException {
        queue.put(item);
        System.out.println("Produced: " + item);
    }

    public void consume() throws InterruptedException {
        int item = queue.take();
        System.out.println("Consumed: " + item);
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueueProducerConsumer pc = new BlockingQueueProducerConsumer();

        Runnable producerTask = () -> {
            try {
                for (int i = 0; i < 20; i++) {
                    pc.produce(i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumerTask = () -> {
            try {
                for (int i = 0; i < 20; i++) {
                    pc.consume();
                    Thread.sleep(15);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        long startTime = System.currentTimeMillis();

        Thread producer = new Thread(producerTask);
        Thread consumer = new Thread(consumerTask);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        long endTime = System.currentTimeMillis();
        System.out.println("BlockingQueue Producer-Consumer Time: " + (endTime - startTime) + "ms");
    }
}
