package com.tistory.data.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.tistory.data.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object RemoteConfigModule {

    private const val CONFIG_CACHE_EXPIRATION_SECONDS = 3600L

    @Provides
    fun provideRemoteConfig() = FirebaseRemoteConfig.getInstance().apply {

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = CONFIG_CACHE_EXPIRATION_SECONDS
        }

        setConfigSettingsAsync(configSettings)
        setDefaultsAsync(R.xml.remote_config_defaults)
    }
}