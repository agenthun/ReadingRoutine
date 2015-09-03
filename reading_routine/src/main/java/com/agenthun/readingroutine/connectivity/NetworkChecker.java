package com.agenthun.readingroutine.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Agent Henry on 2015/9/3.
 */
public class NetworkChecker {
    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;

    public NetworkChecker(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public boolean isConnected() {
        return isConnectedToCellNetwork() || isConnectedToWifiNetwork();
    }

    private boolean isConnectedToCellNetwork() {
        int simState = telephonyManager.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    private boolean isConnectedToWifiNetwork() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }
}
