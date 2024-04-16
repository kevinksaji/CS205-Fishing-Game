package com.example.cs205fishinggame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BubbleUpdaterPool {

    final ExecutorService pool;

    public BubbleUpdaterPool() {
        final int cpuCores = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1);
        pool = Executors.newFixedThreadPool(cpuCores);
    }

    public void submit(final Runnable task) {
        pool.submit(task);
    }
}
