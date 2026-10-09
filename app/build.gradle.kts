plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.example.vitesseapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.vitesseapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature:home"))
    implementation(project(":feature:edit-page"))
    implementation(project(":feature:details"))

    kover(project(":core"))
    kover(project(":feature:home"))
    kover(project(":feature:edit-page"))
    kover(project(":feature:details"))

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "*_HiltModules*",
                    "*_Factory*",
                    "*_Provide*",
                    "*Hilt_*",
                    "*.HiltWrapper_*"
                )
                classes(
                    "*.BuildConfig",
                    "*ComposableSingletons*",
                    "*_Impl*"
                )
                annotatedBy("androidx.compose.ui.tooling.preview.Preview")
            }
        }
    }
}