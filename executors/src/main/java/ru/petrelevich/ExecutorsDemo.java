package ru.petrelevich;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorsDemo {
    private static final Logger log = LoggerFactory.getLogger(ExecutorsDemo.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        tasksList();
        tasksQueue();
        queueHiddenList();
        queueResultList();
        queueHiddenOverFlowList();
        tasksDynamic();
        tasksScheduled();
        useAsParam();
    }

    private static void tasksList() {
        var tasks = List.of("task-t-1", "task-t-2", "task-t-3");

        try (var executor = Executors.newSingleThreadExecutor(makeThreadFactory())) {
            executor.submit(() -> {
                for (var task : tasks) {
                    log.info("{} done", task);
                }
            });
        }
    }

    private static void tasksQueue() {
        var queue = new ConcurrentLinkedQueue<String>();
        queue.add("task-1");
        queue.add("task-2");
        queue.add("task-3");

        try (var executor = Executors.newFixedThreadPool(3, makeThreadFactory())) {
            executor.submit(() -> processItemFromQueue(queue));
            executor.submit(() -> processItemFromQueue(queue));
            executor.submit(() -> processItemFromQueue(queue));
        }
    }

    private static void queueHiddenList() {
        try (var executor = Executors.newFixedThreadPool(3, makeThreadFactory())) {
            executor.submit(() -> log.info("task-1 done queue"));
            executor.submit(() -> log.info("task-2 done queue"));
            executor.submit(() -> log.info("task-3 done queue"));
        }
    }

    private static void queueResultList() throws ExecutionException, InterruptedException {
        try (var executor = Executors.newFixedThreadPool(3, makeThreadFactory())) {
            var res1 = executor.submit(() -> {
                log.info("task-1 done result");
                return "ret-1";
            });
            var res2 = executor.submit(() -> {
                log.info("task-2 done result");
                return "ret-2";
            });
            var res3 = executor.submit(() -> {
                log.info("task-3 done result");
                return "ret-3";
            });

            log.info("results: {}, {}, {}", res1.get(), res2.get(), res3.get());
        }
    }

    private static void queueHiddenOverFlowList() {
        RejectedExecutionHandler rejectedExecution = (Runnable task, ThreadPoolExecutor executor) -> log.warn("rejected task:{}", task);

        try (var executor = new ThreadPoolExecutor(2,
                2,
                0,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2), makeThreadFactory(), rejectedExecution)) {
            executor.submit(task("task-1"));
            executor.submit(task("task-2"));
            executor.submit(task("task-3"));
            executor.submit(task("task-4"));
            executor.submit(task("task-5"));
        }
    }

    private static void tasksDynamic() {
        try (var executor = Executors.newCachedThreadPool(makeThreadFactory())) {
            executor.submit(() -> log.info("task-1 done Cached"));
            executor.submit(() -> log.info("task-2 done Cached"));
            executor.submit(() -> log.info("task-3 done Cached"));
            executor.submit(() -> log.info("task-4 done Cached"));
            executor.submit(() -> log.info("task-5 done Cached"));
            executor.submit(() -> log.info("task-6 done Cached"));
        }
    }

    private static void tasksScheduled() throws InterruptedException {
        try (ScheduledExecutorService executor = Executors.newScheduledThreadPool(1)) {
            executor.scheduleAtFixedRate(() -> log.info("task is done"), 0, 3, TimeUnit.SECONDS);
            var terminationResult =  executor.awaitTermination(6, TimeUnit.SECONDS);
            log.info("terminationResult:{}", terminationResult);
        }
    }

    private static void useAsParam() throws ExecutionException, InterruptedException {
        try (var executor = Executors.newFixedThreadPool(3, makeThreadFactory())) {
            var future = CompletableFuture.supplyAsync(() -> {
                log.info("future done");
                return "future done";
            }, executor);
            log.info("result:{}", future.get());
        }
    }

    private static Runnable task(String name) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Duration.ofSeconds(2));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("{} done ofl", name);
            }

            @Override
            public String toString() {
                return "task:" + name;
            }
        };
    }

    private static void processItemFromQueue(Queue<String> queue) {
        String item;
        do {
            item = queue.poll();
            if (item != null) {
                log.info("{} done again", item);
            }
        } while (!Thread.currentThread().isInterrupted() && item != null);
        log.info("finished");
    }

    private static ThreadFactory makeThreadFactory() {
        AtomicInteger threadId = new AtomicInteger(0);
        return task -> Thread.ofPlatform().name(String.format("thread-%d", threadId.incrementAndGet())).unstarted(task);
    }
}
