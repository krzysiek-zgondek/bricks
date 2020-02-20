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

    testImplementation(project(":test"))
}

apply(from = "../gradle/publishing/publish.gradle.kts")