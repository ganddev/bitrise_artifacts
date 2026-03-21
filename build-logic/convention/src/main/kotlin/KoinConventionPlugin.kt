import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.android.kotlin.multiplatform.library")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.configureEach {
                    when (name) {
                        "commonMain" -> {
                            dependencies {
                                implementation(libs.koin.core)
                                implementation(libs.koin.compose)
                                implementation(libs.koin.viewmodel)
                            }
                        }

                        "androidMain" -> {
                            dependencies {
                                implementation(libs.koin.android)
                            }
                        }
                    }
                }
            }
        }
    }
}