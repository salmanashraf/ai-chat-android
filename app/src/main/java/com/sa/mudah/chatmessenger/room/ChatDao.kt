package com.sa.mudah.chatmessenger.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sa.mudah.chatmessenger.model.MessageModel

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat : MessageModel)

    @Query("SELECT * FROM MessageModel ORDER BY timestamp ASC")
    fun getAllChat() : DataSource.Factory<Int, MessageModel>


    @Query("SELECT * From MessageModel Where timestamp = :userSess")
    fun getMessageForId(userSess : String) : DataSource.Factory<Int, MessageModel>

    @Query("Update MessageModel SET isSuccess = :isSuccess Where id = :msgId")
    fun updateChat(isSuccess : Boolean, msgId: Int)
}
