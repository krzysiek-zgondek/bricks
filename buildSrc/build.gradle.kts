buildscript {
    apply(from = "../gradle/dependencies/default_repositories.gradle.kts")

}

allprojects {
    apply(from = "${rootProject.rootDir}/../gradle/dependencies/default_repositories.gradle.kts")
}