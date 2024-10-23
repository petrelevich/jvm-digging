dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    implementation ("org.springframework.cloud:spring-cloud-config-server")

    implementation ("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")

    implementation("io.micrometer:micrometer-tracing-bridge-otel") // bridges the Micrometer Observation API to OpenTelemetry.
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin") // reports traces to Zipkin.
}
