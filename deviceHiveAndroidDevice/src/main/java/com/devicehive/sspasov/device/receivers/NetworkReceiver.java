package com.devicehive.sspasov.device.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.devicehive.sspasov.device.utils.L;

/**
 * Created by toni on 14.06.15.
 */
public class NetworkReceiver extends BroadcastReceiver {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = NetworkReceiver.class.getSimpleName();

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private static boolean isConnected;
    private static NetworkReceiver mInstance;
    private static Context mContext;
    private static NetworkReceiverListener mNetworkReceiverListener;

    // ---------------------------------------------------------------------------------------------
    // Interfaces
    // ---------------------------------------------------------------------------------------------
    public interface NetworkReceiverListener {
        void onConnection();

        void onLostConnection();
    }

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public static void startReceiver(Context context) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mInstance = new NetworkReceiver();
        mContext = context;
        mContext.registerReceiver(mInstance, filter);
    }

    public static void stopReceiver() {
        if (mInstance != null) {
            mContext.unregisterReceiver(mInstance);
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static void setNetworkReceiverListener(NetworkReceiverListener networkReceiverListener) {
        mNetworkReceiverListener = networkReceiverListener;
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            isConnected = true;
            Toast.makeText(context, "WIFI connected", Toast.LENGTH_LONG).show();
            L.d(TAG, "WIFI connected");
            mNetworkReceiverListener.onConnection();
        } else if (networkInfo != null) {
            isConnected = true;
            Toast.makeText(context, "Mobile Data connected", Toast.LENGTH_LONG).show();
            L.d(TAG, "Mobile Data connected");
            mNetworkReceiverListener.onConnection();
        } else {
            isConnected = false;
            Toast.makeText(context, "Connection lost", Toast.LENGTH_LONG).show();
            L.d(TAG, "Connection lost");
            mNetworkReceiverListener.onLostConnection();
        }
    }
}