import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlinx.kover")
            pluginManager.apply("vitesse.android.ktlint")

            extensions.configure<LibraryExtension> {
                configureAndroidCommon(this)
            }
        }
    }
}
