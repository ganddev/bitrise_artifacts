package de.ahlfeld.bitriseartifacts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform