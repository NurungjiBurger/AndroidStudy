package com.example.androidstudy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int n = 50000;
        ExecutorService executor = Executors.newFixedThreadPool(8);
        Future<?>[] futures = new Future[n];

        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < n; i++) {
            futures[i] = executor.submit(() -> {
                int sum = 0;
                for (int j = 0; j < 10; j++) sum += j;
            });
        }

        for (Future<?> f : futures) f.get();
        executor.shutdown();

        long endTime = System.currentTimeMillis();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Executor Elapsed Time: " + (endTime - startTime) + "ms");
        System.out.println("Memory Usage: " + (endMemory - startMemory) / 1024 + " KB");
    }
}
