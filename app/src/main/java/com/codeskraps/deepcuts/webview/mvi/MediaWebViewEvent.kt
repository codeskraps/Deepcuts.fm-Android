package com.codeskraps.deepcuts.webview.mvi

import android.content.Context
import com.codeskraps.deepcuts.webview.media.MediaWebView

sealed interface MediaWebViewEvent {
    data class WebView(val webView: MediaWebView) : MediaWebViewEvent
    data class Loading(val status: Boolean) : MediaWebViewEvent
    data class ProgressChanged(val progress: Float) : MediaWebViewEvent
    data class Background(val status: Boolean) : MediaWebViewEvent
    data class StartStopService(val context: Context) : MediaWebViewEvent
    data object Permission : MediaWebViewEvent
}