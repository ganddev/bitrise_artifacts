import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.android.kotlin.multiplatform.library")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.configureEach {
                    when (name) {
                        "commonMain" -> {
                            dependencies {
                                implementation(libs.findLibrary("compose-runtime").get())
                                implementation(libs.findLibrary("compose-foundation").get())
                                implementation(libs.findLibrary("compose-material3").get())
                                implementation(libs.findLibrary("compose-ui").get())
                                implementation(libs.findLibrary("compose-components-resources").get())
                                implementation(libs.findLibrary("compose-uiToolingPreview").get())
                                implementation(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                                implementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                            }
                        }
                        "androidMain" -> {
                            dependencies {
                                implementation(libs.findLibrary("compose-uiToolingPreview").get())
                                implementation(libs.findLibrary("androidx-activity-compose").get())
                            }
                        }
                    }
                }
            }

            // For Android debug tools, only if an Android plugin is applied
            pluginManager.withPlugin("com.android.base") {
                dependencies {
                    add("androidRuntimeClasspath", libs.findLibrary("compose-uiTooling").get())
                }
            }
        }
    }
}
