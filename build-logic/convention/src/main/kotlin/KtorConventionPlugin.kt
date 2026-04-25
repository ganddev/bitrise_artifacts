import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            extensions.configure<KotlinMultiplatformExtension> {
                with(sourceSets) {
                    androidMain.dependencies {
                        implementation(project.libs.ktor.client.okhttp)
                    }
                    commonMain.dependencies {
                        implementation(project.libs.ktor.client.core)
                        implementation(project.libs.ktor.client.logging)
                        implementation(project.libs.ktor.client.content.negotiation)
                        implementation(project.libs.ktor.serialization.json)
                    }
                    commonTest.dependencies {
                        implementation(project.libs.ktor.client.mock)
                    }
                }
            }
        }
    }
}
