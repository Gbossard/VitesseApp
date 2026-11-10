plugins {
    alias(libs.plugins.vitesse.android.library)
}

android {
    namespace = "com.example.core.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(libs.junit)
    api(libs.mockk.jvm)
    api(libs.turbine)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}