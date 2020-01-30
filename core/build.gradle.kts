//todo cleanup

plugins {
    id("library")
    id("kotlin")
    id("kotlinx-atomicfu")
}


dependencies {
    compileOnly(gradleApi())

    implementation("junit:junit:4.12")

    /*todo after moving all independent packages to it's own modules this should be moved also*/

    implementation( "org.jetbrains.kotlinx:atomicfu-common:0.14.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")

    implementation("org.koin:koin-android:2.0.1")
}

apply(from = "../gradle/publishing/publish.gradle.kts")