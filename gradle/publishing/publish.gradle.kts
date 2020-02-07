apply(plugin = "org.gradle.maven-publish")

tasks.register<Jar>("sourcesJar") {
    dependsOn("classes")

    project.the<SourceSetContainer>().named("main"){
        from(java.srcDirs)
        archiveClassifier.set("sources")
    }
}

configure<PublishingExtension>() {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifactId = project.name
        }

        //todo pom
    }
}


