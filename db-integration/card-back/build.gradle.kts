plugins {
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("com.google.code.findbugs:jsr305")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("ch.qos.logback:logback-classic")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:21.0.1"
    }

    to {
        image = "localrun/card-back"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(jibBuildTar)
    }
}
