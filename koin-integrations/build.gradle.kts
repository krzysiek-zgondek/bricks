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

    implementation(project(":core"))

    implementation("org.koin:koin-core:2.0.1")
}

apply(from = "../gradle/publishing/publish.gradle.kts")