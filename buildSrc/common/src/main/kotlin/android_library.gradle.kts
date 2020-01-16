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
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    "implementation"(kotlin("stdlib"))
    "testImplementation"("junit:junit:4.12")

    "androidTestImplementation"("androidx.test.ext:junit:1.1.1")
    "androidTestImplementation"("androidx.test.espresso:espresso-core:3.2.0")

    "testImplementation"("org.robolectric:robolectric:4.3.1")
    "testImplementation"("androidx.test:core:1.2.0")
    "testImplementation"("androidx.test:core-ktx:1.2.0")

}
