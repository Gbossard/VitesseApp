plugins {
    alias(libs.plugins.vitesse.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.example.vitesseapp"

    defaultConfig {
        applicationId = "com.example.vitesseapp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
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
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
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
                    "*.HiltWrapper_*",
                    "*.BuildConfig",
                    "*ComposableSingletons*",
                    "*_Impl*"
                )
                packages(
                    "dagger.hilt.*",
                    "hilt_aggregated_deps*"
                )
                annotatedBy("androidx.compose.ui.tooling.preview.Preview")
            }
        }
    }
}
