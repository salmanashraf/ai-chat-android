package com.sa.mudah.chatmessenger.utils

import android.content.Context
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun getISOTimeStamp(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale("ms", "MY"))
    val formattedDate: String = sdf.format(Date())

    return formattedDate
}