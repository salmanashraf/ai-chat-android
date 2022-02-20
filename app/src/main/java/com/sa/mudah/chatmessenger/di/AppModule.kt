package com.sa.mudah.chatmessenger.di

import android.app.Application
import androidx.work.WorkManager
import com.sa.mudah.chatmessenger.api.ApiService
import com.sa.mudah.chatmessenger.api.RetrofitFactory
import com.sa.mudah.chatmessenger.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun providesStarWarsService(): ApiService = RetrofitFactory.getService()

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideChatDao(db: AppDatabase) = db.getChatDao()

}
