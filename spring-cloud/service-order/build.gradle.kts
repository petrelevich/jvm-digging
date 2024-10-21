plugins {
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:21.0.1"
    }

    to {
        image = "localrun/service-order"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(spotlessApply)
        dependsOn(jibBuildTar)
    }
}
