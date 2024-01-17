package com.codeskraps.deepcuts.webview.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.codeskraps.deepcuts.MainActivity
import com.codeskraps.deepcuts.util.Constants
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewEvent
import com.codeskraps.deepcuts.webview.mvi.MediaWebViewState

@Composable
fun HomeButton(
    state: MediaWebViewState
) {
    IconButton(onClick = { state.webView?.loadUrl(Constants.home) }) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Home",
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun RefreshStopButton(
    state: MediaWebViewState
) {
    IconButton(onClick = {
        if (state.loading) {
            state.webView?.stopLoading()
        } else {
            state.webView?.reload()
        }
    }) {
        Icon(
            imageVector = if (state.loading) {
                Icons.Default.Close
            } else {
                Icons.Default.Refresh
            },
            contentDescription = "Stop/Refresh",
            tint = MaterialTheme.colorScheme.tertiary

        )
    }
}

@Composable
fun GoBackButton(
    state: MediaWebViewState
) {
    IconButton(onClick = {
        if (state.webView?.canGoBack() == true) {
            state.webView.goBack()
        }
    }) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "GoBack",
            tint = if (state.webView?.canGoBack() == true) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        )
    }
}

@Composable
fun GoForwardButton(
    state: MediaWebViewState
) {
    IconButton(onClick = {
        if (state.webView?.canGoForward() == true) {
            state.webView.goForward()
        }
    }) {
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "GoForward",
            tint = if (state.webView?.canGoForward() == true) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        )
    }
}

@Composable
fun BackgroundButton(
    state: MediaWebViewState,
    handleEvent: (MediaWebViewEvent) -> Unit
) {

    val context = LocalContext.current
    val activity = context as MainActivity

    IconButton(onClick = {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            handleEvent(MediaWebViewEvent.StartStopService(context))
        } else {
            handleEvent(MediaWebViewEvent.Permission)
        }
    }) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Background",
            tint = if (state.background) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        )
    }
}