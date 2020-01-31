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

    lintOptions {
        isAbortOnError = false
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all{
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core"))

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")

    api("androidx.test:core:1.2.0")
    api("androidx.test:core-ktx:1.2.0")
    api("androidx.arch.core:core-testing:2.1.0")

    api("org.robolectric:robolectric:4.3.1"){
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
}

apply(from = "../gradle/publishing/publish_android.gradle.kts")
