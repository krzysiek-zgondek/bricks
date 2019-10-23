apply(plugin = "digital.wup.android-maven-publish")

configure<PublishingExtension>() {
    publications {
        create("mavenAar", MavenPublication::class) {
            from(project.components["android"])
            artifactId = project.name
        }

        //todo pom
    }
}
