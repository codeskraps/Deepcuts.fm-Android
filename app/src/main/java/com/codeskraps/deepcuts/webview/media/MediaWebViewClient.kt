package com.codeskraps.deepcuts.webview.media

import android.graphics.Bitmap
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.codeskraps.deepcuts.util.Constants
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewEvent

class MediaWebViewClient : WebViewClient() {

    var urlListener: ((String) -> Unit)? = null
    var handleEvent: ((MediaWebViewEvent) -> Unit)? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        handleEvent?.let { it(MediaWebViewEvent.Loading(true)) }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        handleEvent?.let { it(MediaWebViewEvent.Loading(false)) }
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