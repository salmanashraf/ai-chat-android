package com.sa.aichatlib

import android.app.Application
import androidx.room.Room
import com.sa.aichatlib.dao.AppDatabase
import com.sa.aichatlib.repository.ChatRepository

class MyApp : Application() {
	lateinit var repository: ChatRepository
		private set

	override fun onCreate() {
		super.onCreate()
		val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "chat_db").build()
		repository = ChatRepository(db.messageDao())
	}
}

