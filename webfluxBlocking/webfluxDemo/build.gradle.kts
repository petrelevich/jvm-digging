plugins {
    java
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "ru.demo"

repositories {
    mavenLocal()
    mavenCentral()
}

apply(plugin = "io.spring.dependency-management")
dependencyManagement {
    dependencies {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.0.2")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core")
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.projectreactor.tools:blockhound:1.0.7.RELEASE")
    testImplementation("io.projectreactor:reactor-test:3.5.2")
}

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
   }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing", "-Werror"))
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
}

tasks.withType<Test> {
    jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods")
    useJUnitPlatform()
    testLogging.showExceptions = true
    reports {
        junitXml.required.set(true)
        html.required.set(true)
    }
}
