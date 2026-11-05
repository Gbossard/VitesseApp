plugins {
    alias(libs.plugins.vitesse.android.library)
    alias(libs.plugins.vitesse.android.compose)
    alias(libs.plugins.vitesse.android.hilt)
}

android {
    namespace = "com.example.feature.details"
}

dependencies {
    implementation(project(":core"))

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(project(":core:testing"))

    androidTestImplementation(libs.androidx.junit)
}