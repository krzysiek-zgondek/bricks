project.buildscript() {
    //this could be extracted to common kotlin object so you don't have to
    //check name of entry where the version is stored
    val androidPublishVersion by project.extra { "3.6.2" }

    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("digital.wup:android-maven-publish:$androidPublishVersion")
    }
}