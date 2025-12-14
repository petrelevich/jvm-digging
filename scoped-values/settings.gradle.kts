rootProject.name = "scoped-values"

include("scoped-values-demo")


pluginManagement {
    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version "0.10.0-rc03"
        id("io.spring.dependency-management") version "1.1.7"
        id("org.springframework.boot") version "3.5.6"
        id("name.remal.sonarlint") version "6.0.0-rc-2"
        id("com.diffplug.spotless") version "8.0.0"
    }
}