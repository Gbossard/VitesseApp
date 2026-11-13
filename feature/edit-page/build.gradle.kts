plugins {
    alias(libs.plugins.vitesse.android.feature)
}

android {
    namespace = "com.example.feature.edit_page"
}

dependencies {
    // Photo Picker
    implementation(libs.androidx.photopicker.compose)
}
