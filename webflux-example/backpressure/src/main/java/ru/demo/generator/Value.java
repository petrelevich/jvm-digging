package ru.demo.generator;

public record Value(int val) {
    public static final Value COMPLETE_VALUE = new Value(Integer.MIN_VALUE);
}
