import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform() {
    apply(plugin = libs.plugins.kotlinMultiplatform.get().pluginId)
    apply(plugin = libs.plugins.androidKmpLibraryPluging.get().pluginId)
    apply(plugin = libs.plugins.kotlinSerialization.get().pluginId)


    extensions.configure<KotlinMultiplatformExtension> {
        androidLibrary {
            namespace = project.toNamespace()
            compileSdk = libs.versions.android.compileSdk.get().toInt()
            minSdk = libs.versions.android.minSdk.get().toInt()
            compilerOptions.jvmTarget.set(JvmTarget.JVM_17)

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

            withHostTestBuilder {}.configure {
                enableCoverage = true
            }
        }

        jvm()
        iosArm64()
        iosSimulatorArm64()

        with(sourceSets) {
            commonMain.dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
            commonTest.dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        tasks.withType<AbstractTestTask>().configureEach {
            failOnNoDiscoveredTests.set(false)
        }
        configureKotlin()
    }
}

internal fun KotlinMultiplatformExtension.androidLibrary(
    block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit
) {
    configure<KotlinMultiplatformAndroidLibraryTarget>(block)
}