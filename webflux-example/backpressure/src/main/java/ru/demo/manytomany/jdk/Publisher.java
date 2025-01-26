package ru.demo.manytomany.jdk;

import ru.demo.generator.Subscriber;
import ru.demo.generator.Value;

public interface Publisher {
    void subscribe(Subscriber subscriber);

    void onEvent(Value value);

    void onComplete();
}
