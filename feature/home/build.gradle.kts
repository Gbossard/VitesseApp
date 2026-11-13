plugins {
    alias(libs.plugins.vitesse.android.feature)
}

android {
    namespace = "com.example.feature.home"
}

dependencies {
    implementation(libs.androidx.compose.ui)

    // Coil
    implementation(libs.coil.compose)
}
