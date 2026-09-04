plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.core.testing"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(libs.junit)
    api(libs.mockk.jvm)
    api(libs.turbine)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}