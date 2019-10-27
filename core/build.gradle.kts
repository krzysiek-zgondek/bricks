import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins{
    id("library")
    id("kotlin")
}

dependencies{
    compileOnly(gradleApi())
}

apply(from = "../gradle/publishing/publish.gradle.kts")

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}