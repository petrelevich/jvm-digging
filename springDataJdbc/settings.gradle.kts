rootProject.name = "springDataJdbc"

pluginManagement {
    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version "0.10.0-rc03"
        id("io.spring.dependency-management") version "1.1.7"
        id("org.springframework.boot") version "4.0.5"
        id("name.remal.sonarlint") version "7.0.1"
        id("com.diffplug.spotless") version "8.4.0"
    }
}