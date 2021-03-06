//todo cleanup

plugins {
    id("library")
    id("kotlin")
}

sourceSets {
    main {
        java.srcDir("src/main/kotlin")
    }
}

dependencies {
    compileOnly(gradleApi())

    /*todo after moving all independent packages to it's own modules this should be moved also*/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
}

apply(from = "../gradle/publishing/publish.gradle.kts")