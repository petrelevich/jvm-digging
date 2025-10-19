plugins {
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kafka:reactor-kafka")
    implementation("com.fasterxml.jackson.core:jackson-databind")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
        mainClass = "ru.demo.mainpackage.WebfluxKafkaDemo"
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:25"
    }

    to {
        image = "localrun/producer-webflux-kafka"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(jibBuildTar)
    }
}
