plugins {
    java
    id("io.spring.dependency-management") version "1.1.3"
    id("name.remal.sonarlint") version "3.3.13"
    id("com.diffplug.spotless") version "6.22.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.1.5")
        }
    }
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
        options.compilerArgs.add("--add-exports=java.base/jdk.internal.vm.annotation=ALL-UNNAMED")
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
}

spotless {
    java {
        palantirJavaFormat("2.38.0")
    }
}

tasks.withType<Test> {
    jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods")
    testLogging.showExceptions = true
    useJUnitPlatform()
}
