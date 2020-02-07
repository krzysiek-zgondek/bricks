import com.android.build.gradle.LibraryExtension

plugins{
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}


configure<LibraryExtension> {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.2"

    defaultConfig {
        minSdkVersion (21)
        targetSdkVersion (29)
        versionCode = 1
        versionName = `version-full`
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    sourceSets["main"].java.apply{
        srcDir("src/main/kotlin")
        include("**/*.kt")
    }
}

dependencies {
    "implementation"(kotlin("stdlib"))

    "testImplementation"("junit:junit:4.12")

    "androidTestImplementation"("androidx.test.ext:junit:1.1.1")
    "androidTestImplementation"("androidx.test.espresso:espresso-core:3.2.0")
}
