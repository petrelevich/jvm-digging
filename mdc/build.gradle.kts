import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("io.spring.dependency-management")
    id("fr.brouillard.oss.gradle.jgitver")
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
    val springBootDependencies: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootDependencies")
            }
            dependency("io.micrometer:context-propagation:$propagation")
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
            force("com.google.guava:guava:32.1.2-jre")
            force("org.sonarsource.sslr:sslr-core:1.24.0.633")
            force("com.google.code.findbugs:jsr305:3.0.2")
            force("org.eclipse.platform:org.eclipse.osgi:3.18.300")
            force("org.eclipse.platform:org.eclipse.equinox.common:3.17.100")
            force("org.jboss.threads:jboss-threads:3.5.0.Final")
            force("org.wildfly.common:wildfly-common:1.5.4.Final")
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
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }

    apply<name.remal.gradle_plugins.sonarlint.SonarLintPlugin>()
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            palantirJavaFormat("2.38.0")
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