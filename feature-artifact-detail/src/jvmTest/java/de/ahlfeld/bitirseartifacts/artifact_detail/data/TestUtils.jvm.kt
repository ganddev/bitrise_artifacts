package de.ahlfeld.bitirseartifacts.artifact_detail.data

import kotlin.io.bufferedReader
import kotlin.io.readText
import kotlin.use

actual fun readResource(path: String): String {
    return object {}.javaClass.classLoader?.getResourceAsStream(path)?.bufferedReader()?.use { it.readText() }
        ?: throw IllegalArgumentException("Resource not found: $path")
}