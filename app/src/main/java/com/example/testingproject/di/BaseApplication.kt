package com.example.testingproject.di

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.testingproject.LocalHelper
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}