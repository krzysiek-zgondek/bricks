plugins {
    id("library")
    id("kotlin")
}

dependencies {
    compileOnly(gradleApi())
    implementation("junit:junit:4.12")
}

apply(from = "../gradle/publishing/publish.gradle.kts")
