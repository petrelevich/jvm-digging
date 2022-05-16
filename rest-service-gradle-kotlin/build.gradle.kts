buildscript {
    repositories {
        mavenLocal()
        maven {
            credentials {
                username = project.property("gitlabUserMaven") as String?
                password = project.property("gitlabPasswordMaven") as String?
            }
            url = uri("https://gitlab.com/api/v4/projects/27412323/packages/maven")
        }
    }

    dependencies {
        //https://github.com/petrelevich/jvm-digging/tree/master/gradle-base-plugin
        classpath ("base-plugin:base-plugin.gradle.plugin:0.1")
    }
}

plugins {
    java
}

apply(plugin = "base-plugin")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.assertj:assertj-core")
}
