dependencies {
    implementation("ch.qos.logback:logback-classic")

    implementation("io.github.resilience4j:resilience4j-circuitbreaker")
    implementation("io.github.resilience4j:resilience4j-bulkhead")
    implementation("io.github.resilience4j:resilience4j-ratelimiter")
}

tasks {
    bootJar {
        enabled = false
    }
}
