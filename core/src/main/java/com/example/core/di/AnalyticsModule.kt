package com.example.core.di

import com.example.core.analytics.AnalyticsTracker
import com.example.core.analytics.FirebaseAnalyticsTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalyticsTracker(tracker: FirebaseAnalyticsTracker): AnalyticsTracker {
        return tracker
    }
}