plugins{
    id("library")
    id("kotlin")
}

dependencies{
    compileOnly(gradleApi())
}

apply(from = "../gradle/publishing/publish.gradle.kts")