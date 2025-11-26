package com.sa.sampleapp

import android.app.Application
import com.sa.aichatlib.ChatSdk
import com.sa.aichatlib.ChatSdkConfig
import com.sa.aichatlib.initializeWithDefaults

class SampleApp : Application() {
	override fun onCreate() {
		super.onCreate()
		ChatSdk.initializeWithDefaults(this, ChatSdkConfig())
	}
}
