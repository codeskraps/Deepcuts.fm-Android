package com.codeskraps.deepcuts.webview.components

import android.graphics.Bitmap
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.codeskraps.deepcuts.util.Constants
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewEvent

class MediaWebViewClient(
    private val handleEvent: (MediaWebViewEvent) -> Unit
) : WebViewClient() {

    var urlListener: ((String) -> Unit)? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        handleEvent(MediaWebViewEvent.Loading(true))
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        handleEvent(MediaWebViewEvent.Loading(false))
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.let { r ->
            if (r.url.host == Constants.host) {
                urlListener?.let { it(r.url.toString()) }
                return false
            }
        }
        return true
    }
}