plugins{
    id("library")
    id("kotlin")
}

dependencies{
    compileOnly(gradleApi())

    api("org.mockito:mockito-core:3.0.0")

    implementation(kotlin("reflect", embeddedKotlinVersion))
}

apply(from = "../gradle/publishing/publish.gradle.kts")