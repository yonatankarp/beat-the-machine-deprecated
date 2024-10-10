plugins {
    id("beat-the-machine.java-conventions")
    id("beat-the-machine.code-metrics")
    id("beat-the-machine.publishing-conventions")
    id("com.diffplug.spotless") version "6.25.0" apply false
    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
    val kotlinVersion = "2.0.21"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion apply false
}

subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
        maven {
            url = uri("https://maven.pkg.github.com/yonatankarp/beat-the-machine")
            credentials {
                username = findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
                password = findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
