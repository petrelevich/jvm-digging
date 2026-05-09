package ru.demo;

import java.time.LocalDate;

public record Person(String firstName, String lastName, String gender, LocalDate birth) {}
