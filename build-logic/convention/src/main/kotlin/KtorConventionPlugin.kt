import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.getByName("androidMain").dependencies {
                    implementation(libs.findLibrary("ktor-client-okhttp").get())
                }
                sourceSets.getByName("commonMain").dependencies {
                    api(libs.findLibrary("ktor-client-core").get())
                    implementation(libs.findLibrary("ktor-client-logging").get())
                    implementation(libs.findLibrary("ktor-client-content-negotiation").get())
                    implementation(libs.findLibrary("ktor-serialization-json").get())
                }
                sourceSets.getByName("commonTest").dependencies {
                    implementation(libs.findLibrary("ktor-client-mock").get())
                }
            }
        }
    }
}
