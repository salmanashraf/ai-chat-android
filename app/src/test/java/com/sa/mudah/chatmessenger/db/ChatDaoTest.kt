package com.sa.mudah.chatmessenger.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.toLiveData
import androidx.work.ListenableWorker.Result.success
import com.google.common.truth.Truth.assertThat
import com.sa.mudah.chatmessenger.api.Result.Companion.success
import com.sa.mudah.chatmessenger.model.MessageModel
import com.sa.mudah.chatmessenger.repository.ChatRepository
import com.sa.mudah.chatmessenger.room.AppDatabase
import com.sa.mudah.chatmessenger.room.ChatDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ChannelResult.Companion.success
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.annotation.Resource
import javax.inject.Inject
import javax.inject.Named
import kotlin.Result.Companion.success


@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class ChatDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase
    private lateinit var chatDao: ChatDao
    var chatRepo = mockk<ChatRepository>()

    @Before
    fun setup() {
        hiltRule.inject()
        chatDao = database.getChatDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertUser() = runBlockingTest {
        val chat = MessageModel(
            timestamp = "2018-05-29T11:05:00.000Z",
            direction = "INCOMING",
            message = "Hi",
            id = 2,
            createdAt = "2018-05-29T11:05:00.000Z",
            isSuccess = true
        )
        chatDao.insertChat(chat)
        val allUsers = chatDao.getAllChat()
        var data  = allUsers.toLiveData(10)
        assert(data.value!!.isNotEmpty())

    }


}