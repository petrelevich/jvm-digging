plugins {
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("org.apache.kafka:kafka-clients")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("io.micrometer:context-propagation")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
        mainClass = "ru.demo.mainpackage.ConsumerKafkaDemo"
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:25"
    }

    to {
        image = "localrun/spring-boot-demo"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(jibBuildTar)
    }
}
