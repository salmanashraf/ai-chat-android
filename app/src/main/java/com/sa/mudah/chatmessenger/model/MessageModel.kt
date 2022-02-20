package com.sa.mudah.chatmessenger.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Chat(@SerializedName("chat") var chat: ArrayList<MessageModel> = arrayListOf())

@Entity(tableName = "MessageModel", indices = [Index(value = ["timestamp"], unique = true)])
data class MessageModel(


    @ColumnInfo(name = "timestamp")
    var timestamp: String,

    @ColumnInfo(name = "direction")
    var direction: String,

    @ColumnInfo(name = "message")
    var message: String,

    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "createdAt")
    var createdAt: String,

    @ColumnInfo(name = "isSuccess")
    var isSuccess : Boolean = true

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order")
    var order: Int? = null

}