apply(plugin = "org.gradle.maven-publish")

tasks.register<Jar>("sourcesJar") {
    dependsOn("classes")
    from(components["java"])
    archiveClassifier.set("sources")
}

configure<PublishingExtension>() {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = project.name
            from(components["java"])
            artifact(tasks["sourcesJar"])
        }
    }
}
