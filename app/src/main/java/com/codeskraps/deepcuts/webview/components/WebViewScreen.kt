package com.codeskraps.deepcuts.webview.components

import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.codeskraps.deepcuts.util.Constants
import com.codeskraps.deepcuts.util.components.ObserveAsEvents
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewAction
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewEvent
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@Composable
fun WebViewScreen(
    state: MediaWebViewState,
    handleEvent: (MediaWebViewEvent) -> Unit,
    action: Flow<MediaWebViewAction>
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(flow = action) { onAction ->
        when (onAction) {
            is MediaWebViewAction.Toast -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = onAction.message,
                        actionLabel = "Go",
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                    when (result) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            context.startActivity(Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                                addCategory(CATEGORY_DEFAULT)
                                addFlags(FLAG_ACTIVITY_NEW_TASK)
                                addFlags(FLAG_ACTIVITY_NO_HISTORY)
                                addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                            })
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    WebViewProgressIndicator(state = state)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        HomeButton(state = state)
                        RefreshStopButton(state = state)
                        GoBackButton(state = state)
                        GoForwardButton(state = state)
                        Spacer(modifier = Modifier.weight(1f))
                        BackgroundButton(state = state, handleEvent = handleEvent)
                    }
                }
            }
        }
    ) { paddingValues ->
        Row(modifier = Modifier.padding(paddingValues)) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Column {
                    HomeButton(state = state)
                    RefreshStopButton(state = state)
                    GoBackButton(state = state)
                    GoForwardButton(state = state)
                    Spacer(modifier = Modifier.weight(1f))
                    BackgroundButton(state = state, handleEvent = handleEvent)
                }
            }
            Column {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    WebViewProgressIndicator(state = state)
                }

                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { _ ->
                        state.webView?.detachView()
                        state.webView!!.attachView
                    },
                    update = { _ ->
                        state.webView?.iniLoad(Constants.home)
                    }
                )
            }
        }
    }
}
