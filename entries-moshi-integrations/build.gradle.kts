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
    implementation(project(":entries"))
    implementation("com.squareup.moshi:moshi:1.9.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
}

apply(from = "../gradle/publishing/publish.gradle.kts")