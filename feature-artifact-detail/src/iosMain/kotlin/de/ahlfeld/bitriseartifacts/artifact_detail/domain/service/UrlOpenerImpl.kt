package de.ahlfeld.bitriseartifacts.artifact_detail.domain.service

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal class UrlOpenerImpl : UrlOpener {
    override fun openUrl(url: String) {
        try {
            val nsUrl = NSURL.URLWithString(url) ?: return
            UIApplication.sharedApplication.openURL(nsUrl)
        } catch (e: Exception) {
            // Handle error silently - no browser available
        }
    }
}

