plugins {
    java
    id("io.spring.dependency-management") version "1.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "ru.jettydemo"

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

    implementation("org.postgresql:r2dbc-postgresql:1.0.0.RELEASE")
    implementation("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")

    implementation("org.postgresql:postgresql:42.5.1")
    implementation("org.flywaydb:flyway-core")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
}

tasks.withType<Test> {
    jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods")
    useJUnitPlatform()
}
