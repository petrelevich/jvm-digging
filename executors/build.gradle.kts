import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import java.net.URI

plugins {
    java
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    id("fr.brouillard.oss.gradle.jgitver")
    id("name.remal.sonarlint")
    id("com.diffplug.spotless")
}

project.group = "ru.petrelevich"

repositories {
    mavenLocal()
    mavenCentral()
}

apply(plugin = "io.spring.dependency-management")
dependencyManagement {
    imports {
        mavenBom(BOM_COORDINATES)
    }
}
dependencies {
    implementation("ch.qos.logback:logback-classic")
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


configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
}



sonarLint {
    nodeJs {
        detectNodeJs = false
        logNodeJsNotFound = false
    }
}
spotless {
    java {
        palantirJavaFormat("2.39.0")
    }
}


