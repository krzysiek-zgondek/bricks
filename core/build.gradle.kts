//todo cleanup

plugins {
    id("library")
    id("kotlin")
    id("kotlinx-atomicfu")
}


dependencies {
    compileOnly(gradleApi())
    implementation("junit:junit:4.12")
    implementation( "org.jetbrains.kotlinx:atomicfu-common:0.14.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
}

apply(from = "../gradle/publishing/publish.gradle.kts")