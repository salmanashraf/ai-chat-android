package com.sa.aichatlib.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.aichatlib.model.Message
import com.sa.aichatlib.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel()  {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    init {
        loadMessagesFromDb()
    }

    private fun loadMessagesFromDb() {
        viewModelScope.launch {
            repository.messages.collect { messageList ->
                _messages.value = messageList
            }
        }
    }


    fun sendUserMessage(text: String) {
        viewModelScope.launch {
            val userMessage = Message(sender = "User", message = text)
            repository.insert(userMessage)

            _isLoading.value = true
            val aiReply = repository.getAIResponse(text)
            val aiMessage = Message(sender = "AI", message = aiReply)
            repository.insert(aiMessage)
            _isLoading.value = false
        }
    }

}
