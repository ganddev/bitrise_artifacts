package de.ahlfeld.bitriseartifacts.artifact_detail.domain.service

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

internal class UrlOpenerImpl(
    private val context: Context
) : UrlOpener {
    override fun openUrl(url: String) {
        val uri = url.toUri()
        try {
            val customTabsIntent = CustomTabsIntent.Builder().build()

            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            customTabsIntent.launchUrl(context, uri)
        }catch (e: Exception) {
             // Fallback to default browser if Custom Tabs fails
            try {
                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                // Fallback to default browser
            }
        }
    }
}
