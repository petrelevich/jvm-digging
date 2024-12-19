dependencies {
    implementation("ch.qos.logback:logback-classic")

    implementation("io.github.resilience4j:resilience4j-circuitbreaker")
}

tasks {
    bootJar {
        enabled = false
    }
}
