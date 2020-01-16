plugins {
    `kotlin-dsl`
}

apply(from = "gradle/versioning/versioning.gradle.kts")

buildscript {
    apply(from = "gradle/dependencies/android_gradle_plugin.gradle.kts")
    apply(from = "gradle/dependencies/android_maven_publish.gradle.kts")

    dependencies {
        classpath(kotlin("gradle-plugin"))
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.14.1")
    }

}

allprojects {
    apply(from = "${rootProject.rootDir}/gradle/dependencies/default_repositories.gradle.kts")

    group = "com.source.bricks"
    version = `version-full`
}