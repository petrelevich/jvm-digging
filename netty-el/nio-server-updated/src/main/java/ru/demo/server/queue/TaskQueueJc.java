package ru.demo.server.queue;

import ru.demo.server.SystemPropertyUtil;
import org.jctools.queues.MpscChunkedArrayQueue;
import org.jctools.util.Pow2;

import java.util.Queue;

public class TaskQueueJc implements TaskQueueConsumerProducer {

    private final Queue<Runnable> taskQueue;
    private static final int MPSC_CHUNK_SIZE = 1024;
    protected static final int DEFAULT_MAX_PENDING_TASKS = Math.max(16,
            SystemPropertyUtil.getInt("io.netty.eventLoop.maxPendingTasks", Pow2.MAX_POW2));

    public TaskQueueJc() {
        taskQueue = new MpscChunkedArrayQueue<>(MPSC_CHUNK_SIZE, DEFAULT_MAX_PENDING_TASKS);
    }

    @Override
    public boolean offerTask(Runnable task) {
        return taskQueue.offer(task);
    }


    @Override
    public Runnable pollTask() {
        return taskQueue.poll();
    }

}
