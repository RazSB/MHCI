package com.sae.raz.util;

import android.net.ConnectivityManager;

public class NetworkConnectionUtil {
    private static ConnectivityManager connectivityManager;

    public static String NETWORK_ERROR = "Network connection error";

    public static void initializeNetworkConnectionUtil(ConnectivityManager _connectivityManager) {
        connectivityManager = _connectivityManager;
    }

    public static boolean isOnline() {
        return (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected());
    }
}
