plugins{
    `java-library`
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    "implementation"(kotlin("stdlib"))
    "testImplementation"("junit:junit:4.12")
}
