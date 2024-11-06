plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks {
    build {
        dependsOn(spotlessApply)
    }
}
