plugins {
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter")
    implementation ("org.apache.kafka:kafka-clients")
    implementation ("ch.qos.logback:logback-classic")
    implementation ("com.fasterxml.jackson.core:jackson-databind")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:21.0.1"
    }

    to {
        image = "localrun/request-processor"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(jibBuildTar)
    }
}
