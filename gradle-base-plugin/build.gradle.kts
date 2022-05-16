plugins {
    kotlin("jvm") version "1.5.31"
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("maven-publish")
}

project.group = "ru.project"
project.version = "0.1"

repositories {
    mavenLocal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("base-plugin") {
            id = "base-plugin"
            implementationClass = "ru.plugins.BasePlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            credentials {
                username = project.property("gitlabUserMaven") as String?
                password = project.property("gitlabPasswordMaven") as String?
            }
            url = uri("https://gitlab.com/api/v4/projects/27412323/packages/maven")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.6.7")
}


tasks.getByName("build").dependsOn("publishToMavenLocal")
