plugins {
    `kotlin-dsl`
}

apply(from = "gradle/versioning/versioning.gradle.kts")

buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }

    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath("digital.wup:android-maven-publish:3.6.2")
        classpath(kotlin("gradle-plugin"))
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }

    group = "com.source.bricks"
    version = `version-full`
}