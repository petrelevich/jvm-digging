plugins {
    id ("org.springframework.boot")
    id ("com.google.cloud.tools.jib")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
        mainClass = "ru.demo.RestService"
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:25"
    }

    to {
        image = "localrun/rest-service"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(jibBuildTar)
    }
}
