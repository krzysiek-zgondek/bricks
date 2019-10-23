import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(plugin = "org.gradle.kotlin.kotlin-dsl")
////
plugins {
    kotlin("jvm")
}

dependencies {
    "implementation"(kotlin("stdlib-jdk8"))
}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
