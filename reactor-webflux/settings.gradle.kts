rootProject.name = "reactor-webflux"

include("reactor-demo")
include("webflux-demo")

pluginManagement {
    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version "0.10.0-rc03"
        id("io.spring.dependency-management") version "1.1.7"
        id("org.springframework.boot")  version "4.0.6"
        id("name.remal.sonarlint") version "7.1.0"
        id("com.diffplug.spotless") version "8.4.0"
    }
}