package com.sa.mudah.chatmessenger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp : Application(){

    companion object {
        var appInstance: MainApp? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this

    }
}
