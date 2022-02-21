package com.sa.mudah.chatmessenger

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp : Application(){


    companion object {
        var appInstance: MainApp? = null
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var context: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        context = applicationContext

    }
}
