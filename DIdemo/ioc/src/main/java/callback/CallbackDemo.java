package callback;

import java.util.concurrent.Callable;

public class CallbackDemo {

    public static void main(String[] args) throws Exception {
        new CallbackDemo().someAction("va1", () -> "call1");

        new CallbackDemo().someAction("va1", () -> "call2");
    }

    public void someAction(String value, Callable<String> callback) throws Exception {
        System.out.println(value);
        String someResult = callback.call();
        System.out.println("someResult:" + someResult);
    }
}
