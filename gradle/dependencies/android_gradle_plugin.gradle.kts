project.buildscript() {
    //this could be extracted to common kotlin object so you don't have to
    //check name of entry where the version is stored
    val buildToolVersion by project.extra { "3.5.1" }

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$buildToolVersion")
    }
}