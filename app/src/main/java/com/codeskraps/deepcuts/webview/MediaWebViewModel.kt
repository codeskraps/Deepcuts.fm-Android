package com.codeskraps.deepcuts.webview

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.codeskraps.deepcuts.ForegroundService
import com.codeskraps.deepcuts.util.BackgroundStatus
import com.codeskraps.deepcuts.util.Constants
import com.codeskraps.deepcuts.util.StateReducerViewModel
import com.codeskraps.deepcuts.webview.components.MediaWebChromeClient
import com.codeskraps.deepcuts.webview.components.MediaWebView
import com.codeskraps.deepcuts.webview.components.MediaWebViewClient
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewAction
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewEvent
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaWebViewModel @Inject constructor(
    mediaWebView: MediaWebView,
    private val backgroundStatus: BackgroundStatus
) : StateReducerViewModel<MediaWebViewState, MediaWebViewEvent, MediaWebViewAction>() {

    override fun initState(): MediaWebViewState = MediaWebViewState.initial

    init {
        mediaWebView.setMediaWebChromeClient(MediaWebChromeClient(state::handleEvent))
        mediaWebView.setMediaWebViewClient(MediaWebViewClient(state::handleEvent))

        state.handleEvent(MediaWebViewEvent.WebView(mediaWebView))

        viewModelScope.launch(Dispatchers.IO) {
            backgroundStatus.status.collect {
                Log.v("MediaWebViewModel", "status collected: $it")
                state.handleEvent(MediaWebViewEvent.Background(it))
            }
        }
    }

    override fun reduceState(
        currentState: MediaWebViewState,
        event: MediaWebViewEvent
    ): MediaWebViewState {
        return when (event) {
            is MediaWebViewEvent.WebView -> currentState.copy(webView = event.webView)
            is MediaWebViewEvent.Loading -> currentState.copy(loading = event.status)
            is MediaWebViewEvent.ProgressChanged -> currentState.copy(progress = event.progress)
            is MediaWebViewEvent.Background -> currentState.copy(background = event.status)
            is MediaWebViewEvent.StartStopService -> onStartStopService(currentState, event.context)
            is MediaWebViewEvent.Permission -> onPermission(currentState)
        }
    }

    private fun onStartStopService(
        currentState: MediaWebViewState,
        context: Context
    ): MediaWebViewState {
        val url = currentState.webView?.url
        viewModelScope.launch(Dispatchers.IO) {
            if (!currentState.background) {
                ContextCompat.startForegroundService(
                    context,
                    Intent(context, ForegroundService::class.java).apply {
                        putExtra(Constants.inputExtra, url)
                    }
                )
            } else {
                context.stopService(Intent(context, ForegroundService::class.java))
            }
        }
        return currentState
    }

    private fun onPermission(currentState: MediaWebViewState): MediaWebViewState {
        viewModelScope.launch(Dispatchers.IO) {
            actionChannel.send(MediaWebViewAction.Toast("Allow Notification Permission in the Device Settings for the app"))
        }
        return currentState
    }
}