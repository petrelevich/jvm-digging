dependencies {
    implementation ("io.projectreactor.kafka:reactor-kafka")
    implementation ("ch.qos.logback:logback-classic")
    implementation ("com.fasterxml.jackson.core:jackson-databind")

    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.assertj:assertj-core")

    testImplementation ("org.testcontainers:kafka")
    testImplementation("io.projectreactor.tools:blockhound")
    testImplementation("io.projectreactor:reactor-test")
}


