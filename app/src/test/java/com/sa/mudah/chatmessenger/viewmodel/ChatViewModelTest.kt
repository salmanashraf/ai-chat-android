package com.sa.mudah.chatmessenger.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.sa.mudah.chatmessenger.repository.ChatRepository
import com.sa.mudah.chatmessenger.util.TestCoroutineRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class ChatViewModelTest {
    /**
     * Live Data & Coroutine test thread configuration
     */

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    /**
     * Mock
     */
    private val context = mockk<Application>()
    private val chatRepository = mockk<ChatRepository>()

    /**
     * View Model Under Test
     */
    lateinit var chatViewModel: ChatViewModel

    @Before
    fun setUp() {
        chatViewModel = ChatViewModel(
            chatRepository, ioDispatcher = testCoroutineRule.testDispatcher
        )
    }

    @Test
    fun sendMessageToServer() {
        every {
            chatRepository.sendMessage(any(), any())
        } returns
                chatViewModel.sendAndReceiveChat(any())

        verify { chatRepository.sendMessage(any(), any())}
    }

    @After
    fun after() {
    }

}