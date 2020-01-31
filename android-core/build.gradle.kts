plugins{
    id("android_library")
}

android{
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all{
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation(project(":core"))

    testImplementation(project(":test-android"))
    testImplementation(project(":test"))

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
}

apply(from = "../gradle/publishing/publish_android.gradle.kts")
