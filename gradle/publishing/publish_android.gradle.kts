import com.android.build.gradle.LibraryExtension

apply(plugin = "digital.wup.android-maven-publish")


val androidSourcesJar by tasks.registering(Jar::class){
    project.the<LibraryExtension>().apply{
        from(sourceSets["main"].java.srcDirs)
        archiveClassifier.set("sources")
    }
}

configure<PublishingExtension>() {
    publications {
        create("mavenAar", MavenPublication::class) {
            from(project.components["android"])
            artifactId = project.name

            artifact(androidSourcesJar.get())
        }

        //todo pom
    }
}
