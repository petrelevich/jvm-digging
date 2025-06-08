package ru.demo.server;

public interface EventLoopChooser {
    EventLoop next();
}
