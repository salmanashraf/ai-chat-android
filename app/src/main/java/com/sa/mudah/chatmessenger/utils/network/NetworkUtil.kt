package com.sa.mudah.chatmessenger.utils.network

import android.content.Context
import android.net.ConnectivityManager
import com.sa.mudah.chatmessenger.MainApp.Companion.context
import com.sa.mudah.chatmessenger.R

object NetworkUtil {
    @JvmField
    var TYPE_WIFI = 1

    @JvmField
    var TYPE_MOBILE = 2

    @JvmField
    var TYPE_NOT_CONNECTED = 0

    @JvmStatic
    @Suppress("DEPRECATION")
    fun getConnectivityStatus(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        if (conn == TYPE_WIFI) {
            status = context.getString(R.string.wifi_enabled)
        } else if (conn == TYPE_MOBILE) {
            status = context.getString(R.string.mobile_data_enabled)
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = context.getString(R.string.no_internet)
        }
        return status
    }

    @Suppress("DEPRECATION")
    val isInternetAvailable: Boolean
        get() {
            val connectivityManager =
                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
        }
}