plugins{
    `kotlin-dsl`
}

rootProject.dependencies {
    runtime (project(path))
}

dependencies{
    compileOnly(gradleApi())
    compileOnly(kotlin("gradle-plugin"))
}