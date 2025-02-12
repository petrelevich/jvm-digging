dependencies {
    implementation("ch.qos.logback:logback-classic")

    implementation("io.github.resilience4j:resilience4j-circuitbreaker")
    implementation("io.github.resilience4j:resilience4j-bulkhead")
}

tasks {
    bootJar {
        enabled = false
    }
}
