plugins {
    java
    id("io.spring.dependency-management") version "1.1.0"
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
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.0.5")
        }
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("org.ehcache:ehcache:3.10.6")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.5")

    testImplementation("org.assertj:assertj-core")
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
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
    useJUnitPlatform()
    testLogging.showExceptions = true
    reports {
        junitXml.required.set(true)
        html.required.set(true)
    }
}

tasks {
    val managedVersions by registering {
        doLast {
            project.extensions.getByType<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>()
                .managedVersions
                .toSortedMap()
                .map { "${it.key}:${it.value}" }
                .forEach(::println)
        }
    }
}