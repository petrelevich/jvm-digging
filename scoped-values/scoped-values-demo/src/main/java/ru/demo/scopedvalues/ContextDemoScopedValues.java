package ru.demo.scopedvalues;

import static java.lang.ScopedValue.where;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextDemoScopedValues {
    private static final Logger log = LoggerFactory.getLogger(ContextDemoScopedValues.class);

    private static final ScopedValue<Context> contextSV = ScopedValue.newInstance();

    static void main() throws InterruptedException {
        var contextDemoSingleton = new ContextDemoScopedValues();

        Thread request1 = Thread.ofPlatform().name("t1").unstarted(() -> where(contextSV, new Context())
                .run(() -> {
                    contextDemoSingleton.methodA();
                    contextDemoSingleton.methodB();
                    contextDemoSingleton.methodC();
                    var context = contextSV.get();
                    log.info("req1: counter:{}, params:{}", context.getCounter(), context.getParams());
                }));

        Thread request2 = Thread.ofPlatform().name("t2").unstarted(() -> where(contextSV, new Context())
                .run(() -> {
                    contextDemoSingleton.methodA();
                    contextDemoSingleton.methodB();
                    contextDemoSingleton.methodC();
                    var context = contextSV.get();
                    log.info("req2: counter:{}, params:{}", context.getCounter(), context.getParams());
                }));

        request1.start();
        request2.start();

        request1.join();
        request2.join();
    }

    private void methodA() {
        log.info("methodA");
        var context = contextSV.get();
        context.increment();
        context.setValue("A", 1);
    }

    private void methodB() {
        log.info("methodB");
        var context = contextSV.get();
        context.increment();
        context.setValue("B", 2);
    }

    private void methodC() {
        log.info("methodC");
        var context = contextSV.get();
        context.increment();
        context.setValue("C", 3);
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
