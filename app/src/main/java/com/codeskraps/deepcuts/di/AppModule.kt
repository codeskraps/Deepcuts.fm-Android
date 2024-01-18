package com.codeskraps.deepcuts.di

import android.app.Application
import com.codeskraps.deepcuts.util.BackgroundStatus
import com.codeskraps.deepcuts.webview.media.MediaWebView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesMediaWebView(
        application: Application
    ): MediaWebView {
        return MediaWebView(application)
    }

    @Provides
    @Singleton
    fun providesBackgroundStatus(): BackgroundStatus {
        return BackgroundStatus()
    }
}