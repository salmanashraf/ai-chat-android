package com.sa.mudah.chatmessenger

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sa.mudah.chatmessenger.chat.ChatAdapter
import com.sa.mudah.chatmessenger.databinding.ActivityMainBinding
import com.sa.mudah.chatmessenger.model.Chat
import com.sa.mudah.chatmessenger.model.MessageModel
import com.sa.mudah.chatmessenger.utils.getISOTimeStamp
import com.sa.mudah.chatmessenger.utils.getJsonDataFromAsset
import com.sa.mudah.chatmessenger.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*


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


        etMessage.addTextChangedListener(object : TextWatcher {
            var isTyping = false
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            private var timer: Timer = Timer()
            private val DELAY: Long = 60000 // 1 minute
            override fun afterTextChanged(s: Editable) {
                if (!isTyping) {
                    if(BuildConfig.DEBUG)
                    Timber.d("started typing")
                    isTyping = true
                }
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            isTyping = false
                            if(BuildConfig.DEBUG)
                                Timber.d("Stop typing")

                            val message = MessageModel(
                                getISOTimeStamp(), "INCOMING",
                                "Are you there?", 34, getISOTimeStamp(), true
                            )
//
                            etMessage.setText("")
                            chatViewModel.sendAndReceiveChat(message)
                        }
                    },
                    DELAY
                )
            }
        })

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