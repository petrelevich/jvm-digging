plugins {
    id ("org.springframework.boot")
    id ("com.google.cloud.tools.jib")
}

dependencies {
//  implementation ("org.springframework.boot:spring-boot-starter-web")

    implementation ("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation ("org.springframework.boot:spring-boot-starter-jetty")

//    implementation ("org.springframework.boot:spring-boot-starter-web") {
//        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
//    }
//    implementation ("org.springframework.boot:spring-boot-starter-undertow")
}

jib {
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:21.0.1"
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
