package com.example.hungerhalter.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context): Boolean {

        // To get information of currently active network devices
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // To check for current active networks
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        // To check connectivity of Network
        if(activeNetwork?.isConnected != null) {
            return activeNetwork.isConnected
        } else {
            return false
        }
    }
}