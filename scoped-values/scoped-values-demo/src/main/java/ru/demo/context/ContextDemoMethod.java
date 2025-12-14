package ru.demo.context;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextDemoMethod {
    private static final Logger log = LoggerFactory.getLogger(ContextDemoMethod.class);

    private ContextDemoMethod() {}

    static void main() {
        var context = new Context();
        var contextDemo = new ContextDemoMethod();
        contextDemo.methodA(context);
        contextDemo.methodB(context);
        contextDemo.methodC(context);
        log.info("counter:{}, params:{}", context.getCounter(), context.getParams());
    }

    private void methodA(Context context) {
        context.increment();
        context.setValue("A", 1);
    }

    private void methodB(Context context) {
        context.increment();
        context.setValue("B", 2);
    }

    private void methodC(Context context) {
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
