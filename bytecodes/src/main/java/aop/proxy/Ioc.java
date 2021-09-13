package aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class Ioc {

    private Ioc() {
    }

    static MyClassInterface createMyClass() {
        return (MyClassInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(),
                new Class<?>[]{MyClassInterface.class},
                new DemoInvocationHandler()
        );
    }

    private static class DemoInvocationHandler implements InvocationHandler {
        private final MyClassInterface myClass = new MyClassImpl();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("invoking method:" + method + " args: " + Arrays.toString(args));
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
