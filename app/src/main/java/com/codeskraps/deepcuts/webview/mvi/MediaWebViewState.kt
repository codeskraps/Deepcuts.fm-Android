package com.codeskraps.deepcuts.webview.mvi

import com.codeskraps.deepcuts.webview.components.MediaWebView

data class MediaWebViewState (
    val loading: Boolean,
    val webView: MediaWebView?,
    val progress: Float,
    val background: Boolean
) {
    companion object {
        val initial = MediaWebViewState(
            loading = false,
            webView = null,
            progress = .0f,
            background = false
        )
    }
}
