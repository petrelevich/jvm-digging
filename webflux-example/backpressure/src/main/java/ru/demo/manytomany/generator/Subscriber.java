package ru.demo.manytomany.generator;


public interface Subscriber {
    void onNext(Value t);

    void onComplete();
}
