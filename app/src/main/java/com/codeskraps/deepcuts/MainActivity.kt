package com.codeskraps.deepcuts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codeskraps.deepcuts.ui.theme.TurntableTheme
import com.codeskraps.deepcuts.webview.MediaWebViewModel
import com.codeskraps.deepcuts.webview.components.WebViewScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TurntableTheme {
                val viewModel = hiltViewModel<MediaWebViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                WebViewScreen(
                    state = state,
                    handleEvent = viewModel.state::handleEvent,
                    action = viewModel.action
                )
            }
        }
    }
}

