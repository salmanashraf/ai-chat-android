package com.sa.mudah.chatmessenger.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class DebounceExtKtTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun clear() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should call only once when time between calls is shorter than debounce delay`() =
        testDispatcher.runBlockingTest {
            // given
            val debounceTime = 500L
            val timeBetweenCalls = 200L
            val firstParam = 1
            val secondParam = 2
            val testFun = mock<(Int) -> Unit> {
                onGeneric { invoke(any()) } doReturn Unit
            }
            val debouncedTestFun = debounce(
                debounceTime,
                MainScope(),
                testFun
            )
            // when
            debouncedTestFun(firstParam)
            advanceTimeBy(timeBetweenCalls)
            debouncedTestFun(secondParam)
            // then
            verify(testFun, times(1)).invoke(any())
        }

    @Test
    fun `should call every time with proper params when time between calls is longer than debounce delay`() =
        testDispatcher.runBlockingTest {
            // given
            val debounceTime = 500L
            val timeBetweenCalls = 1000L
            val firstParam = 1
            val secondParam = 2
            val testFun = mock<(Int) -> Unit> {
                onGeneric { invoke(any()) } doReturn Unit
            }
            val debouncedTestFun = debounce(
                debounceTime,
                MainScope(),
                testFun
            )

            // when
            debouncedTestFun(firstParam)
            advanceTimeBy(timeBetweenCalls)
            debouncedTestFun(secondParam)
            // then
            verify(testFun).invoke(firstParam)
            verify(testFun).invoke(secondParam)
        }
}

