package com.example.core.di

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.core.analytics.AnalyticsTracker
import com.example.core.analytics.FirebaseAnalyticsTracker
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @RequiresPermission(Manifest.permission.WAKE_LOCK)
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAnalyticsTracker(tracker: FirebaseAnalyticsTracker): AnalyticsTracker {
        return tracker
    }
}