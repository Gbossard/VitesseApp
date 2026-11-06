import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("vitesse.android.library")
            pluginManager.apply("vitesse.android.compose")
            pluginManager.apply("vitesse.android.hilt")

            dependencies {
                add("implementation", project(":core"))
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("testImplementation", project(":core:testing"))
                add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
            }
        }
    }
}