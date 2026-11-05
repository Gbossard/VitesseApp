plugins {
    alias(libs.plugins.vitesse.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.feature.details"
}

dependencies {
    implementation(project(":core"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(project(":core:testing"))

    androidTestImplementation(libs.androidx.junit)
}