import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES


plugins {
    id("fr.brouillard.oss.gradle.jgitver")
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    id("name.remal.sonarlint") apply false
    id("com.diffplug.spotless")
}


allprojects {
    group = "ru.demo"

    repositories {
        mavenCentral()
    }

    val jetty = "12.1.9"
    val redisson = "4.3.1"
    val jetbrains = "26.1.0"
    val guava = "33.6.0-jre"
    val log4j = "2.26.0"
    val disruptor = "4.0.0"

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            dependency("com.google.guava:guava:$guava") // required for palantirJavaFormat
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.eclipse.jetty:jetty-bom:$jetty")
                mavenBom("org.apache.logging.log4j:log4j-bom:$log4j")
            }
            dependency("org.jetbrains:annotations:$jetbrains")
            dependency("org.redisson:redisson:$redisson")

            dependency("com.lmax:disruptor:$disruptor")
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()

            force("javax.servlet:servlet-api:2.5")
            force("commons-logging:commons-logging:1.1.1")
            force("commons-lang:commons-lang:2.5")
            force("org.codehaus.jackson:jackson-core-asl:1.8.8")
            force("org.codehaus.jackson:jackson-mapper-asl:1.8.8")
            force("commons-io:commons-io:2.18.0")
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-parameters", "-Xlint:all,-serial,-processing"))

        dependsOn("spotlessApply")
    }
    apply<name.remal.gradle_plugins.sonarlint.SonarLintPlugin>()

    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            palantirJavaFormat()
        }
    }

    plugins.apply(fr.brouillard.oss.gradle.plugins.JGitverPlugin::class.java)
    extensions.configure<fr.brouillard.oss.gradle.plugins.JGitverPluginExtension> {
        strategy("PATTERN")
        nonQualifierBranches("main,master")
        tagVersionPattern("\${v}\${<meta.DIRTY_TEXT}")
        versionPattern(
            "\${v}\${<meta.COMMIT_DISTANCE}\${<meta.GIT_SHA1_8}" +
                    "\${<meta.QUALIFIED_BRANCH_NAME}\${<meta.DIRTY_TEXT}-SNAPSHOT"
        )
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
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