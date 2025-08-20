package com.example.androidstudy;

public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        int n = 50000;
        Thread[] threads = new Thread[n];

        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(() -> {
                int sum = 0;
                for (int j = 0; j < 10; j++) sum += j;
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        long endTime = System.currentTimeMillis();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Thread Elapsed Time: " + (endTime - startTime) + " ms");
        System.out.println("Memory Usage: " + (endMemory - startMemory) / 1024 + " KB");
    }
}

