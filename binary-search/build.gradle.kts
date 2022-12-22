import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    `java`
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

group = "ru.petrelevich.binary-search"

repositories {
    mavenLocal()
    mavenCentral()
}

tasks {
    val hello by registering {
        doLast {
            println("hello task")
        }
    }
}