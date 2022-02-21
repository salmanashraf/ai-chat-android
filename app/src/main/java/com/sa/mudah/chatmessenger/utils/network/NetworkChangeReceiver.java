package com.sa.mudah.chatmessenger.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static OnNetworkChangeListener listener;

    public void setListener(OnNetworkChangeListener listener2) {
        listener = listener2;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        if (listener != null) {
            if (conn == NetworkUtil.TYPE_WIFI) {
                listener.onInternetWifi();
            } else if (conn == NetworkUtil.TYPE_MOBILE) {
                listener.onInternetMobile();
            } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
                listener.onNoInternet();
            }
        }
    }

    public interface OnNetworkChangeListener {
        void onInternetWifi();

        void onInternetMobile();

        void onNoInternet();
    }
}
