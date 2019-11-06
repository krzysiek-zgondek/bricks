plugins {
    `kotlin-dsl`
}

buildscript {
    apply(from = "../../gradle/dependencies/android_gradle_plugin.gradle.kts")
}

rootProject.dependencies {
    runtime(project(path))
}

dependencies {
    compileOnly(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation("com.android.tools.build:gradle:${project.extra["buildToolVersion"]}")
}