package com.sa.mudah.chatmessenger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sa.mudah.chatmessenger.model.Chat
import com.sa.mudah.chatmessenger.utils.getJsonDataFromAsset
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sa.mudah.chatmessenger.chat.ChatAdapter
import com.sa.mudah.chatmessenger.databinding.ActivityMainBinding
import com.sa.mudah.chatmessenger.model.MessageModel
import com.sa.mudah.chatmessenger.utils.getISOTimeStamp
import com.sa.mudah.chatmessenger.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var chatRVAdapter: ChatAdapter
    private lateinit var chatViewModel: ChatViewModel

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideKeyboard()

        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        preLoadChat()
        getChats()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        rvMsg.layoutManager = layoutManager

        chatRVAdapter = ChatAdapter()
        rvMsg.adapter = chatRVAdapter

        fabSend.setOnClickListener {

            if (!TextUtils.isEmpty(etMessage.editableText)) {

                val message = MessageModel(
                    getISOTimeStamp(), "INCOMING",
                    etMessage.editableText.toString().trim(), 33, getISOTimeStamp(), true
                )

                etMessage.setText("")
                chatViewModel.sendAndReceiveChat(message)


            }
        }

    }

    private fun getChats() {

        chatViewModel.chatList.observe(this) {
            chatRVAdapter.submitList(it)

            if (it.size > 0)
                rvMsg.smoothScrollToPosition(it.size - 1)
        }

        chatViewModel.serverErrorOutput.error.observe(this) {
            Toast.makeText(this, "$it ", Toast.LENGTH_LONG).show()
        }
    }

    private fun hideKeyboard() {

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun preLoadChat() {
        val preLoadedChat = getJsonDataFromAsset(this, "chat.json")
        val gson = Gson()


        val listType: Type = object : TypeToken<Chat?>() {}.type
        val list: Chat = gson.fromJson(preLoadedChat, listType)
        for (i in 0 until list.chat.size) run {

            val message = MessageModel(
                list.chat[i].timestamp,
                list.chat[i].direction,
                list.chat[i].message, -1, getISOTimeStamp(), true
            )
            chatViewModel.savePreLoadedChat(message)
        }

    }

}