package ru.calculator.bench;

@SuppressWarnings("java:S106")
class GcDemoControl implements GcDemoControlMBean {
    private final GcDemo gcDemo;

    public GcDemoControl(GcDemo gcDemo) {
        this.gcDemo = gcDemo;
    }

    @Override
    public int getObjectArraySize() {
        int size = gcDemo.getObjectArraySize();
        System.out.println("current size:" + size);
        return size;
    }

    @Override
    public void setObjectArraySize(int size) {
        System.out.println("setting size:" + size);
        gcDemo.setObjectArraySize(size);
    }
}
