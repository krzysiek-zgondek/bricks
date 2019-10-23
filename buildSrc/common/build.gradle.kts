plugins{
    `kotlin-dsl`
}

buildscript{
    repositories{
        google()
        jcenter()
    }
    dependencies{
        classpath("com.android.tools.build:gradle:3.5.0")
    }
}

rootProject.dependencies {
    runtime (project(path))
}

dependencies{
    compileOnly(gradleApi())
    compile(kotlin("gradle-plugin"))
    compile("com.android.tools.build:gradle:3.5.0")
}