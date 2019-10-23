import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.named("compileKotlin", KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<KotlinCompile>("compileTestKotlin") {
    kotlinOptions.jvmTarget = "1.8"
}
//
//dependencies {
//    "implementation"(kotlin("stdlib-jdk8"))
//}
//
//repositories {
//    mavenCentral()
//}
