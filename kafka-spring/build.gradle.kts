import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel


plugins {
    idea
    id("io.spring.dependency-management")
    id("name.remal.sonarlint") apply false
    id("com.diffplug.spotless") apply false
    id("fr.brouillard.oss.gradle.jgitver") apply false
    id("org.springframework.boot") apply false
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


allprojects {
    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
            }
        }
    }
    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()

            force("org.sonarsource.analyzer-commons:sonar-analyzer-commons:2.4.0.1317")
            force("com.google.code.findbugs:jsr305:3.0.2")
            force("org.sonarsource.sslr:sslr-core:1.24.0.633")
            force("org.eclipse.platform:org.eclipse.osgi:3.18.300")
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing", "-Werror"))
    }

    apply<name.remal.gradle_plugins.sonarlint.SonarLintPlugin>()
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            googleJavaFormat("1.16.0").aosp()
        }
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