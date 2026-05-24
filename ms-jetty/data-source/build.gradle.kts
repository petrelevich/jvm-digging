plugins {
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
    id("fr.brouillard.oss.gradle.jgitver")
}


apply(plugin = "com.google.cloud.tools.jib")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:21.0.1"
    }

    to {
        image = "localrun/data-source"
        tags = setOf(project.version.toString())
    }
}

tasks {
    build {
        dependsOn(spotlessApply)
        dependsOn(jibBuildTar)
    }
}