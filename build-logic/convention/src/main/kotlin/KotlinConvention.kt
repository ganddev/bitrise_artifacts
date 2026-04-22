import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    configureKotlin()
}

/**
 * Configure base Kotlin options
 */
internal fun Project.configureKotlin() {
    extensions.configure<KotlinProjectExtension> {
        jvmToolchain(17)
    }

    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-Xinline-classes",
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xjvm-default=all"
                )
            )

            if (name.contains("test", ignoreCase = true)) {
                freeCompilerArgs.add("-java-parameters")
            }
        }
    }
}