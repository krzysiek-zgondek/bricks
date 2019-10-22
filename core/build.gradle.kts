plugins{
    `java-library`
    kotlin("jvm")
}

apply(from = "../buildSrc/config/versioning.gradle.kts")
apply(from = "../buildSrc/config/publishing/publish.gradle.kts")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}