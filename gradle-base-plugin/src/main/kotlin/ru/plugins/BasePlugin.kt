package ru.plugins

import io.spring.gradle.dependencymanagement.internal.dsl.StandardDependencyManagementExtension

import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.getByName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.repositories

class BasePlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        repositories {
            mavenLocal()
            mavenCentral()

            maven {
                credentials {
                    username = project.property(Versions.packagesPropUserName) as String?
                    password = project.property(Versions.packagesPropPassword) as String?
                }
                url = uri(Versions.packagesUrl)
            }

        }
        group = Versions.projectGroupName

        project.extensions.getByName<JavaPluginExtension>("java").apply {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        project.apply(plugin = "org.springframework.boot")
        project.apply(plugin = "io.spring.dependency-management")
        project.extensions.getByName<StandardDependencyManagementExtension>("dependencyManagement").apply {
            imports {
                mavenBom("io.projectreactor:reactor-bom:${Versions.reactor}")
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Versions.springCloud}")
                mavenBom("org.testcontainers:testcontainers-bom:${Versions.testcontainers}")
            }

            dependencies {
                dependency("com.google.code.findbugs:jsr305:${Versions.jsr305}")
            }
        }

        tasks.named<Test>("test") {
            useJUnitPlatform()
            testLogging.showExceptions = true
            reports {
                junitXml.required.set(true)
                html.required.set(true)
            }
        }

        configurations.all {
            resolutionStrategy {
                failOnVersionConflict()

                force("net.java.dev.jna:jna:5.8.0")
                force("org.ow2.asm:asm:9.1")
                force("org.checkerframework:dataflow-errorprone:3.22.0")
                force("com.google.errorprone:error_prone_annotations:2.13.1")
                force("org.checkerframework:checker-qual:3.19.0")
                force("com.google.guava:guava:31.0.1-jre")
            }
        }
    }
}
