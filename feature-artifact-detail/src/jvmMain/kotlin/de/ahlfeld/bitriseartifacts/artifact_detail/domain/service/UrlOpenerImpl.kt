package de.ahlfeld.bitriseartifacts.artifact_detail.domain.service

import java.awt.Desktop
import java.net.URI

internal class UrlOpenerImpl : UrlOpener {
    override fun openUrl(url: String) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(url))
            }
        } catch (e: Exception) {
            // Handle error silently
        }
    }
}

