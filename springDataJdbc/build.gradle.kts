import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel


plugins {
    java
    idea
    id("io.spring.dependency-management")
    id("name.remal.sonarlint")
    id("com.diffplug.spotless")
    id("fr.brouillard.oss.gradle.jgitver")
    id("org.springframework.boot")
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(17)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
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

val testcontainersBom: String by project

apply(plugin = "io.spring.dependency-management")
dependencyManagement {
    dependencies {
        imports {
            mavenBom(BOM_COORDINATES)
            mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
        }
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("com.google.code.findbugs:jsr305")

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()

        force("com.google.guava:guava:32.1.2-jre")
        force("com.google.code.findbugs:jsr305:3.0.2")
        force("org.eclipse.platform:org.eclipse.osgi:3.18.300")
        force("org.eclipse.platform:org.eclipse.equinox.common:3.17.100")
        force("org.sonarsource.sslr:sslr-core:1.24.0.633")
    }
}

jgitver {
    strategy("PATTERN")
    nonQualifierBranches("main,master")
    tagVersionPattern("\${v}\${<meta.DIRTY_TEXT}")
    versionPattern(
        "\${v}\${<meta.COMMIT_DISTANCE}\${<meta.GIT_SHA1_8}" +
                "\${<meta.QUALIFIED_BRANCH_NAME}\${<meta.DIRTY_TEXT}-SNAPSHOT"
    )
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing", "-Werror"))
}

spotless{
    java {
        palantirJavaFormat("2.38.0")
    }
}

tasks.test {
    jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods")
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
            project.extensions.getByType<DependencyManagementExtension>()
                .managedVersions
                .toSortedMap()
                .map { "${it.key}:${it.value}" }
                .forEach(::println)
        }
    }
}