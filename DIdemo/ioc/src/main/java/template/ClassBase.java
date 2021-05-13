package template;

public abstract class ClassBase {

    protected abstract String internalMethod();

    public void process() {
        System.out.println("something before");
        System.out.println(internalMethod());
        System.out.println("something after");
    }
}
