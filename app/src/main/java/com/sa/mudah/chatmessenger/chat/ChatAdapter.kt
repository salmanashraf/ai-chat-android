package com.sa.mudah.chatmessenger.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sa.mudah.chatmessenger.databinding.ActivityMainBinding
import com.sa.mudah.chatmessenger.databinding.RvReceivedMessageBinding
import com.sa.mudah.chatmessenger.databinding.RvSentMessageBinding
import com.sa.mudah.chatmessenger.model.MessageModel
import com.sa.mudah.chatmessenger.utils.RECEIVED_MESSAGE
import com.sa.mudah.chatmessenger.utils.SENT_MESSAGE

class ChatAdapter : PagedListAdapter<MessageModel, RecyclerView.ViewHolder>(diffCallback) {


    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            holderTypeMessageSent -> {
                val binding = RvSentMessageBinding.inflate(layoutInflater, parent, false)
                SentViewHolder(binding)
            }
            holderTypeMessageReceived -> {
                val binding = RvReceivedMessageBinding.inflate(layoutInflater, parent, false)
                ReceivedViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val chat = getItem(position)
        when (holder.itemViewType) {
            holderTypeMessageSent -> chat?.let {
                (holder as SentViewHolder).bind(
                    it
                )
            }
            holderTypeMessageReceived -> chat?.let {
                (holder as ReceivedViewHolder).bind(
                    it
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList!![position]?.direction == "INCOMING") {
            holderTypeMessageSent
        } else {
            holderTypeMessageReceived
        }
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<MessageModel>() {
            override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean =
                oldItem == newItem
        }
    }

    class ReceivedViewHolder(private val binding: RvReceivedMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MessageModel) {
            binding.chat = viewModel
            binding.executePendingBindings()
        }
    }

    class SentViewHolder(private val binding: RvSentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MessageModel) {
            binding.chat = viewModel
            binding.executePendingBindings()
        }
    }

}

