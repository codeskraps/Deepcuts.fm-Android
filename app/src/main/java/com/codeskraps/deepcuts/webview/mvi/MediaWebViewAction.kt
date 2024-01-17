package com.codeskraps.deepcuts.webview.mvi

sealed interface MediaWebViewAction {
    data class Toast(val message: String) : MediaWebViewAction
}