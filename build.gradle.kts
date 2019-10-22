plugins {
    `kotlin-dsl`
}

buildscript {
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:3.5.0")
        classpath (kotlin("gradle-plugin"))
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }

    group = "com.source.bricks"
    version = "0.0"
}