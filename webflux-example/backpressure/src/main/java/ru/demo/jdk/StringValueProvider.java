package ru.demo.jdk;


public interface StringValueProvider {
    void request(int n);

    void consume(StringValueConsumer consumer);

    void subscribe();
}
