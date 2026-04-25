import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureKotlinMultiplatform()

            extensions.configure<KotlinMultiplatformExtension> {
                with(sourceSets) {
                    commonMain.dependencies {
                        implementation(libs.jetbrains.navigation.compose)
                    }
                }
            }
        }
    }
}
