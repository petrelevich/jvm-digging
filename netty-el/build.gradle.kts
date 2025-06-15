import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("fr.brouillard.oss.gradle.jgitver")
    id("io.spring.dependency-management")
    id("name.remal.sonarlint") apply false
    id("com.diffplug.spotless") apply false
}

allprojects {
    group = "ru.petrelevich"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val springBootBom: String by project
    val spotbugs: String by project
    val jctools: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootBom")
            }
            dependency ("com.github.spotbugs:spotbugs-annotations:$spotbugs")
            dependency ("org.jctools:jctools-core:$jctools")
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()

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