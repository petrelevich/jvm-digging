package ru.demo.context;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextDemoThreadLocal {
    private static final Logger log = LoggerFactory.getLogger(ContextDemoThreadLocal.class);

    private final ThreadLocal<Context> contextTl = new ThreadLocal<>();

    static void main() throws InterruptedException {
        var contextDemoSingleton = new ContextDemoThreadLocal();

        Thread request1 = Thread.ofPlatform().name("t1").unstarted(() -> {
            var context = new Context();
            contextDemoSingleton.set(context);
            contextDemoSingleton.methodA();
            contextDemoSingleton.methodB();
            contextDemoSingleton.methodC();
            contextDemoSingleton.close(); // не забыть
            log.info("req1: counter:{}, params:{}", context.getCounter(), context.getParams());
        });

        Thread request2 = Thread.ofPlatform().name("t2").unstarted(() -> {
            var context = new Context();
            contextDemoSingleton.set(context);
            contextDemoSingleton.methodA();
            contextDemoSingleton.methodB();
            contextDemoSingleton.methodC();
            contextDemoSingleton.close(); // не забыть
            log.info("req2: counter:{}, params:{}", context.getCounter(), context.getParams());
        });

        request1.start();
        request2.start();

        request1.join();
        request2.join();
    }

    private void methodA() {
        log.info("methodA");
        var context = contextTl.get();
        context.increment();
        context.setValue("A", 1);
    }

    private void methodB() {
        log.info("methodB");
        var context = contextTl.get();
        context.increment();
        context.setValue("B", 2);
    }

    private void methodC() {
        log.info("methodC");
        var context = contextTl.get();
        context.increment();
        context.setValue("C", 3);
    }

    private void set(Context context) {
        contextTl.set(context);
    }

    private void close() {
        contextTl.remove();
    }

    private static class Context {
        private int counter = 0;
        private final Map<String, Integer> params = new HashMap<>();

        private Context() {}

        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }

        public void setValue(String name, int value) {
            params.put(name, value);
        }

        public Map<String, Integer> getParams() {
            return params;
        }
    }
}
