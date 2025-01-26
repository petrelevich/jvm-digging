package ru.petrelevich;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadsDemo {
    private static final Logger log = LoggerFactory.getLogger(ThreadsDemo.class);

    public static void main(String[] args) {
        basic();
        tasksList();
        tasksQueue();
    }

    private static void basic() {
        Thread.ofPlatform().name("thread-1").start(() -> log.info("task 1 done"));
        Thread.ofPlatform().name("thread-2").start(() -> log.info("task 2 done"));
        Thread.ofPlatform().name("thread-3").start(() -> log.info("task 3 done"));
    }

    private static void tasksList() {
        var tasks = List.of("task-1", "task-2", "task-3");

        Thread.ofPlatform().name("thread").start(() -> {
            for (var task : tasks) {
                log.info("{} done", task);
            }
        });
    }

    private static void tasksQueue() {
        var queue = new ConcurrentLinkedQueue<String>();
        queue.add("task-1");
        queue.add("task-2");
        queue.add("task-3");

        Thread.ofPlatform().name("thread-1").start(() -> processItemFromQueue(queue));
        Thread.ofPlatform().name("thread-2").start(() -> processItemFromQueue(queue));
        Thread.ofPlatform().name("thread-3").start(() -> processItemFromQueue(queue));
    }

    private static void processItemFromQueue(Queue<String> queue) {
        String item;
        do {
            item = queue.poll();
            if (item != null) {
                log.info("{} done again", item);
            }
        } while(!Thread.currentThread().isInterrupted() && item != null);
        log.info("finished");
    }
}
