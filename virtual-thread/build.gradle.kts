import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    id("fr.brouillard.oss.gradle.jgitver")
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    id("name.remal.sonarlint") apply false
    id("com.diffplug.spotless") apply false
}

allprojects {
    group = "ru.demo"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val propagation: String by project
    val commonsIo: String by project
    val palantir: String by project
    val guava: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
            }
            dependency("io.micrometer:context-propagation:$propagation")
            dependency("commons-io:commons-io:$commonsIo")
            dependency("com.palantir.javaformat:palantir-java-format:$palantir")
            dependency("com.google.guava:guava:$guava")
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    plugins.withType<JavaPlugin> {
        project.dependencies.add(
            "compileOnly",
            "com.palantir.javaformat:palantir-java-format"
        )
    }
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
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