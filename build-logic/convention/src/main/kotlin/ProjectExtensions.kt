import org.gradle.api.Project

private val PATH_DELIMITERS = arrayOf(":", "-")
private const val NAMESPACE_DELIMITER = "."
private const val NAMESPACE = "de.ahlfeld.bitrise.%s"

fun Project.toNamespace(): String {
    val name = path.split(delimiters = PATH_DELIMITERS)
        .joinToString(NAMESPACE_DELIMITER)

    return NAMESPACE.format(name)
}