plugins {
    id ("org.springframework.boot")
    id ("com.google.cloud.tools.jib")
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
        mainClass = "ru.demo.CalculationsVThread"
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:21"
    }

    to {
        image = "localrun/calculations-vthread"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(jibBuildTar)
    }
}
