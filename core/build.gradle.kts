plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("appyx-publish-android")
    id("appyx-lint")
    id("appyx-detekt")
}

android {
    namespace = "com.bumble.appyx.core"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
    testOptions {
        unitTests.all {
            // interface method default implementation
            it.exclude("**/*\$DefaultImpls.class")
        }
    }
}

dependencies {
    api(project(":customisations"))
    api(libs.kotlin.coroutines.android)
    api(libs.androidx.lifecycle.common)
    api(libs.compose.animation.core)
    api(libs.compose.runtime)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.java8)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.foundation)

    testImplementation(project(":testing-junit4"))
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)

    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(project(":testing-ui"))
}
