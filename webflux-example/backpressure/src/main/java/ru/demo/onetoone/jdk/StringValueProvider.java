package ru.demo.onetoone.jdk;


public interface StringValueProvider {
    void request(int n);

    void consume(StringValueConsumer consumer);
}
