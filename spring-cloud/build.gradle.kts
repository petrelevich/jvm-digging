import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import name.remal.gradle_plugins.sonarlint.SonarLintExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    idea
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

    val springCloudVersion: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
            }
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()

            force("com.google.guava:guava:32.1.2-jre")

            force("org.apache.httpcomponents:httpclient:4.5.14")
            force("org.glassfish.hk2:hk2-api:3.1.1")
            force("com.fasterxml.woodstox:woodstox-core:6.5.1")
            force("commons-logging:commons-logging:1.3.1")
            force("org.glassfish.hk2:hk2-utils:3.1.1")
            force("org.glassfish.hk2.external:aopalliance-repackaged:3.1.1")

//            force("commons-logging:commons-logging:1.1.1")
//            force("commons-lang:commons-lang:2.5")
//            force("org.codehaus.jackson:jackson-core-asl:1.8.8")
//            force("org.codehaus.jackson:jackson-mapper-asl:1.8.8")
//            force("org.sonarsource.analyzer-commons:sonar-analyzer-commons:2.8.0.2699")
//            force("org.sonarsource.analyzer-commons:sonar-xml-parsing:2.8.0.2699")
//            force("org.sonarsource.sslr:sslr-core:1.24.0.633")
//            force("org.sonarsource.analyzer-commons:sonar-analyzer-recognizers:2.8.0.2699")
//            force("com.google.code.findbugs:jsr305:3.0.2")
//            force("commons-io:commons-io:2.15.1")
//            force("com.google.errorprone:error_prone_annotations:2.26.1")
//            force("com.google.j2objc:j2objc-annotations:3.0.0")
        }
    }
}

subprojects {
    plugins.apply(SpringBootPlugin::class.java)
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
    configure<SonarLintExtension> {
        nodeJs {
            detectNodeJs = false
            logNodeJsNotFound = false
        }
    }
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