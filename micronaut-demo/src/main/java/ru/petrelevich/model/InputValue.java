package ru.petrelevich.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record InputValue (String val){}

