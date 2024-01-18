package com.codeskraps.deepcuts.webview.media

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewEvent

@SuppressLint("SetJavaScriptEnabled")
class MediaWebView(context: Context) {

    private val webView by lazy {
        InternalWebView(context).apply {
            webViewClient = MediaWebViewClient()
            webChromeClient = MediaWebChromeClient()
        }
    }

    private var initLoad: Boolean = false
    val attachView: View
        get() = webView
    val url: String?
        get() = webView.url

    fun iniLoad(url: String) {
        if (!initLoad) {
            initLoad = true
            loadUrl(url)
        }
    }

    fun setUrlListener(urlListener: ((String) -> Unit)?) {
        (webView.webViewClient as MediaWebViewClient).urlListener = urlListener
    }

    fun setHandleListener(handleEvent: ((MediaWebViewEvent) -> Unit)?) {
        (webView.webChromeClient as MediaWebChromeClient).handleEvent = handleEvent
        (webView.webViewClient as MediaWebViewClient).handleEvent = handleEvent
    }

    fun detachView() {
        webView.parent?.let {
            (it as ViewGroup).removeView(webView)
        }
    }

    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    fun stopLoading() {
        webView.stopLoading()
    }

    fun reload() {
        webView.reload()
    }

    fun canGoBack(): Boolean = webView.canGoBack()
    fun canGoForward(): Boolean = webView.canGoForward()

    fun goBack() {
        webView.goBack()
    }

    fun goForward() {
        webView.goForward()
    }

    class InternalWebView : WebView {
        constructor(context: Context?) : super(context!!)
        constructor(context: Context?, attrs: AttributeSet?) : super(
            context!!, attrs
        )

        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context!!, attrs, defStyleAttr
        )

        override fun onWindowVisibilityChanged(visibility: Int) {
            if (visibility != View.GONE && visibility != View.INVISIBLE) {
                super.onWindowVisibilityChanged(visibility)
            }
        }

        init {
            with(settings) {
                loadsImagesAutomatically = true
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
                mediaPlaybackRequiresUserGesture = false

                //setInitialScale(300)
                setSupportZoom(true)
                setNetworkAvailable(true)
            }

            CookieManager.getInstance().setAcceptThirdPartyCookies(this@InternalWebView, true)
        }
    }
}