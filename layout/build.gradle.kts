apply(from = "../buildSrc/config/versioning.gradle.kts")

plugins{
    `java-library`
    kotlin("jvm")
    `maven-publish`
}

group = "com.source.bricks"
version = "0.0"

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allJava)
    archiveClassifier.set("sources")
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(tasks["sourcesJar"])
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}