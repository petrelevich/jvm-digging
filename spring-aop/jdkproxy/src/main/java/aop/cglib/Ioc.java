package aop.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

class Ioc {

    MyClass createMyClass(BeforeAction beforeAction) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyClass.class);
        var myClass = new MyClass();
        enhancer.setCallback(
                (MethodInterceptor)
                        (obj, method, args, proxy) -> {
                            beforeAction.action(method, args);
                            return method.invoke(myClass, args);
                        });
        return (MyClass) enhancer.create();
    }
}
