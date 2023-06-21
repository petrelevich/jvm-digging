dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("ch.qos.logback:logback-classic")
    implementation("net.sourceforge.htmlunit:htmlunit:2.70.0")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.github.spotbugs:spotbugs-annotations:4.7.3")

    compileOnly ("org.projectlombok:lombok")
    testCompileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("org.awaitility:awaitility:4.2.0")

}

