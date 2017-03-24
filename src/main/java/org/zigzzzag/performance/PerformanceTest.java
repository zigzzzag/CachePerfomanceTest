package org.zigzzzag.performance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {

    static final long START_TIME = System.currentTimeMillis();
    private static final int THREAD_COUNT = 100;
    private static final int MAX_BRANCH_COUNT = 300_000;

    private PerformanceTest() {
    }

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int startBranchIndex = MAX_BRANCH_COUNT / THREAD_COUNT * i;
            final int finishBranchIndex = MAX_BRANCH_COUNT / THREAD_COUNT * (i + 1);
            executorService.submit(new BranchLoader(startBranchIndex, finishBranchIndex));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
}
