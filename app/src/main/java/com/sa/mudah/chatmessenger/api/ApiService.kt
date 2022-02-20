package com.sa.mudah.chatmessenger.api

import com.sa.mudah.chatmessenger.model.MessageModel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/** We are not using suspend function of kotlin like in
 * previous versions as work manager is itself executing
 * this call in background so need of couroutine calling this function anymore */

/** This apikey value can be changed to show error */

interface ApiService {
    @POST("users/")
    fun sendMessage(
        @Query("message") message: String): Single<MessageModel>
}
