package dev.robert.games.utils

import android.content.Context
import android.content.Intent

fun Context.shareLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
    }
    startActivity(Intent.createChooser(shareIntent, null))
}