import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    java
    id("io.spring.dependency-management")
    id("name.remal.sonarlint")
    id("com.diffplug.spotless")
    id("fr.brouillard.oss.gradle.jgitver")
    id("org.springframework.boot")
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

val spotbugs: String by project

apply(plugin = "io.spring.dependency-management")
dependencyManagement {
    dependencies {
        imports {
            mavenBom(BOM_COORDINATES)
        }
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("org.postgresql:postgresql")
    implementation("com.github.spotbugs:spotbugs-annotations:$spotbugs")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-flyway")


    implementation("org.flywaydb:flyway-database-postgresql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
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
    options.compilerArgs.addAll(listOf("-parameters", "-Xlint:all,-serial,-processing", "-Werror"))

    dependsOn("spotlessApply")
}

spotless {
    java {
        googleJavaFormat()
    }
}