package com.kooapps.mininumbergame.model.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static final String GOOGLE_PING_COMMAND = "ping -c 1 google.com";

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
