package aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class Ioc {

    MyClassInterface createMyClass(BeforeAction beforeAction) {
        InvocationHandler handler = new DemoInvocationHandler(new MyClassImpl(), beforeAction);
        return (MyClassInterface)
                Proxy.newProxyInstance(
                        Ioc.class.getClassLoader(),
                        new Class<?>[] {MyClassInterface.class},
                        handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final MyClassInterface myClass;
        private final BeforeAction beforeAction;

        public DemoInvocationHandler(MyClassInterface myClass, BeforeAction beforeAction) {
            this.myClass = myClass;
            this.beforeAction = beforeAction;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            beforeAction.action(method, args);
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
