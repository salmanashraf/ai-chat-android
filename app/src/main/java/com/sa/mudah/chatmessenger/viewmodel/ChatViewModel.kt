package com.sa.mudah.chatmessenger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.sa.mudah.chatmessenger.di.IoDispatcher
import com.sa.mudah.chatmessenger.model.MessageModel
import com.sa.mudah.chatmessenger.output.BaseOutput
import com.sa.mudah.chatmessenger.output.SingleLiveEvent
import com.sa.mudah.chatmessenger.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
public class ChatViewModel  @Inject constructor(
    private val chatRepository: ChatRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel()  {

    val chatList : LiveData<PagedList<MessageModel>>  = chatRepository.getChatList()
    var serverErrorOutput = BaseOutput<String>()

    private val compositeDisposable = CompositeDisposable()

    init {
        serverErrorOutput = chatRepository.serverErrorOutput
    }

    fun savePreLoadedChat(messageModel: MessageModel) {
        viewModelScope.launch(ioDispatcher) {
            chatRepository.savePreLoadedChat(messageModel)
        }
    }

    fun sendAndReceiveChat(messageModel: MessageModel) {
        viewModelScope.launch(ioDispatcher) {
            chatRepository.sendMessage(compositeDisposable, messageModel)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }




}