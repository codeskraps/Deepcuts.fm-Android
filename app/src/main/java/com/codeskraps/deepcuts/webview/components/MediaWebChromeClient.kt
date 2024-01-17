package com.codeskraps.deepcuts.webview.components

import android.webkit.WebChromeClient
import android.webkit.WebView
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewEvent

class MediaWebChromeClient(
    private val handleEvent: (MediaWebViewEvent) -> Unit
) : WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        handleEvent(MediaWebViewEvent.ProgressChanged(newProgress.toFloat()))
    }
}