package aop.proxy;

import java.lang.reflect.Method;

public interface BeforeAction {
    void action(Method method, Object[] args);
}
