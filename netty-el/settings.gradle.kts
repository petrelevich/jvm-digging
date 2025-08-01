rootProject.name = "netty-el"

include("client")
include("netty-server")
include("nio-server")
include("nio-server-updated")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val sonarlint: String by settings
    val spotless: String by settings
    val johnrengelmanShadow: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    }
}
