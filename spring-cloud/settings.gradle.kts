pluginManagement {
    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version "0.10.0-rc03"
        id("io.spring.dependency-management") version "1.1.5"
        id("org.springframework.boot") version "3.3.1"
        id("name.remal.sonarlint") version "4.2.4"
        id("com.diffplug.spotless") version "6.25.0"
        id("com.google.cloud.tools.jib") version "3.4.3"
    }
}
rootProject.name = "spring-cloud"

include("api-gateway")
include("config-server")
include("eureka-server")
include("service-client")
include("service-client-info")
include("service-order")

include("kafka-log-appender")
include("resilience4j")
include("bucket4j")
include("bucket4j-redis")
