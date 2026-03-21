import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.android.kotlin.multiplatform.library")
            }


            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.configureEach {
                    when (name) {
                        "commonMain" -> {
                            dependencies {
                                implementation(project.libs.androidx.lifecycle.viewmodelCompose)
                                implementation(project.libs.androidx.lifecycle.runtimeCompose)
                                implementation(project.libs.compose.runtime)
                                implementation(project.libs.compose.foundation)
                                implementation(project.libs.compose.material3)
                                implementation(project.libs.compose.ui)
                                implementation(project.libs.compose.components.resources)
                                implementation(project.libs.compose.uiToolingPreview)
                            }
                        }

                        "androidMain" -> {
                            dependencies {
                                implementation(project.libs.androidx.activity.compose)
                                implementation(project.libs.androidx.compose.uiTooling)
                                implementation(project.libs.compose.uiToolingPreview)
                            }
                        }
                    }
                }
            }

            // For Android debug tools, only if an Android plugin is applied
            pluginManager.withPlugin("com.android.base") {
                dependencies {
                    add("androidRuntimeClasspath", project.libs.androidx.compose.uiTooling)
                }
            }

            // For KMP Android library plugin
            pluginManager.withPlugin("com.android.kotlin.multiplatform.library") {
                dependencies {
                    // Try to add it to common configurations that might be used for rendering previews
                    runCatching {
                        add(
                            "androidRuntimeClasspath",
                            project.libs.androidx.compose.uiTooling
                        )
                    }
                }
            }
        }
    }
}
