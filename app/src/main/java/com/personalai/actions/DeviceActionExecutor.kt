package com.personalai.actions

import android.content.Context
import android.content.Intent
import android.net.Uri

class DeviceActionExecutor(private val context: Context) {

    fun execute(actionHint: String): Boolean {
        val normalized = actionHint.lowercase()

        return when {
            normalized.startsWith("open:") -> {
                openUrl(normalized.removePrefix("open:").trim())
            }
            normalized.startsWith("search:") -> {
                webSearch(normalized.removePrefix("search:").trim())
            }
            else -> false
        }
    }

    private fun openUrl(url: String): Boolean {
        val safeUrl = if (url.startsWith("http")) url else "https://$url"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
        return true
    }

    private fun webSearch(query: String): Boolean {
        val encoded = Uri.encode(query)
        return openUrl("https://www.google.com/search?q=$encoded")
    }
}
