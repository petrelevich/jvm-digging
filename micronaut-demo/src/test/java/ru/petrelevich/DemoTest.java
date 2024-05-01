package ru.petrelevich;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

class DemoTest extends DatabaseTestBase {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        assertThat(application.isRunning()).isTrue();
    }
}
