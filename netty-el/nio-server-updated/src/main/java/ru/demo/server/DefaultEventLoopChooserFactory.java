package ru.demo.server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("java:S6548") //перенес из netty "как есть"
public class DefaultEventLoopChooserFactory {

    public static final DefaultEventLoopChooserFactory INSTANCE = new DefaultEventLoopChooserFactory();

    private DefaultEventLoopChooserFactory() {
    }

    public EventLoopChooser newChooser(EventLoop[] executors) {
        if (isPowerOfTwo(executors.length)) {
            return new PowerOfTwoEventLoopChooser(executors);
        } else {
            return new GenericEventLoopChooser(executors);
        }
    }

    private static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }

    private static final class PowerOfTwoEventLoopChooser implements EventLoopChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventLoop[] executors;

        PowerOfTwoEventLoopChooser(EventLoop[] executors) {
            this.executors = executors;
        }

        @Override
        public EventLoop next() {
            return executors[idx.getAndIncrement() & executors.length - 1];
        }
    }

    private static final class GenericEventLoopChooser implements EventLoopChooser {
        private final AtomicLong idx = new AtomicLong();
        private final EventLoop[] executors;

        GenericEventLoopChooser(EventLoop[] executors) {
            this.executors = executors;
        }

        @Override
        public EventLoop next() {
            return executors[(int) Math.abs(idx.getAndIncrement() % executors.length)];
        }
    }
}