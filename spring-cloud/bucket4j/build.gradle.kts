dependencies {
    implementation("ch.qos.logback:logback-classic")

    implementation("com.bucket4j:bucket4j_jdk17-core")
}

tasks {
    bootJar {
        enabled = false
    }
}
