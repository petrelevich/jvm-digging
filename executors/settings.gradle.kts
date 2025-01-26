pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("org.springframework.boot") version "3.4.2"
        id("io.spring.dependency-management") version "1.1.7"
        id("fr.brouillard.oss.gradle.jgitver") version "0.10.0-rc03"
        id("name.remal.sonarlint") version "5.1.0"
        id("com.diffplug.spotless") version "7.0.2"

    }
}
rootProject.name = "executors"

