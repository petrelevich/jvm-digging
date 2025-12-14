package ru.demo.context;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextDemoField {
    private static final Logger log = LoggerFactory.getLogger(ContextDemoField.class);

    private final Context context;

    private ContextDemoField(Context context) {
        this.context = context;
    }

    static void main() {
        var context = new Context();
        var contextDemo = new ContextDemoField(context);
        contextDemo.methodA();
        contextDemo.methodB();
        contextDemo.methodC();
        log.info("counter:{}, params:{}", context.getCounter(), context.getParams());
    }

    private void methodA() {
        context.increment();
        context.setValue("A", 1);
    }

    private void methodB() {
        context.increment();
        context.setValue("B", 2);
    }

    private void methodC() {
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
