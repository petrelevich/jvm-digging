package ru.demo.generator;


public interface Subscriber {
    void onNext(Value t);

    void onComplete();

    String getName();
}
