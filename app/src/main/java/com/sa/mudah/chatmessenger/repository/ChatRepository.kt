package com.sa.mudah.chatmessenger.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bumptech.glide.load.HttpException
import com.sa.mudah.chatmessenger.api.BaseDataSource
import com.sa.mudah.chatmessenger.api.ApiService
import com.sa.mudah.chatmessenger.model.MessageModel
import com.sa.mudah.chatmessenger.output.BaseOutput
import com.sa.mudah.chatmessenger.room.ChatDao
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HttpsURLConnection

@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao
) : BaseDataSource() {

    @Inject
    lateinit var apiServices: ApiService
    var serverErrorOutput = BaseOutput<String>()

    val MESSAGE_PER_PAGE = 20


    fun savePreLoadedChat(sentMsgModel: MessageModel) {
        insertChat(sentMsgModel)
    }

    fun insertChat(messageModel: MessageModel?) {

        messageModel?.let {
            chatDao.insertChat(messageModel)
        }
    }

    val config = PagedList.Config.Builder()
        .setPageSize(MESSAGE_PER_PAGE)
        .build()

    fun getChatList(): LiveData<PagedList<MessageModel>> {
        return LivePagedListBuilder(chatDao.getAllChat(), config).build()
    }

    fun sendMessage(compositeDisposable: CompositeDisposable, sentMsgModel: MessageModel) {
        try {
            compositeDisposable.add(
                apiServices.sendMessage(sentMsgModel.message)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            insertChat(sentMsgModel)
                        }, {
                            handleApiError(it)
                            sentMsgModel.isSuccess = false
                            insertChat(sentMsgModel)

                        })
            )
        } catch (e: Exception) {
            sentMsgModel.isSuccess = false
            insertChat(sentMsgModel)
        }
    }


    fun handleApiError(error: Throwable) {
        if (error is HttpException) {
            when ((error as HttpException).statusCode) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> serverErrorOutput.error.postValue("Unauthorised User ")
                HttpsURLConnection.HTTP_FORBIDDEN -> serverErrorOutput.error.postValue("Forbidden")
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> serverErrorOutput.error.postValue("Internal Server Error")
                HttpsURLConnection.HTTP_BAD_REQUEST -> serverErrorOutput.error.postValue("Bad Request")
                else -> serverErrorOutput.error.postValue(error.localizedMessage)
            }
        } else {
            serverErrorOutput.error.postValue("Bad Request")
        }
    }
}
