package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.devicehive.sspasov.device.utils.L;

/**
 * Created by toni on 23.05.15.
 */
public class Battery {
    private static final String TAG = Battery.class.getSimpleName();
    private final String name = "value";
    private String value;
    private Context mContext;

    private static Battery instance;

    public static Battery getInstance() {
        if (instance == null) {
            instance = new Battery();
        }
        return instance;
    }

    public static Battery getInstance(Context context) {
        if (instance == null) {
            instance = new Battery(context);
        }
        return instance;
    }

    public Battery() {
    }


    public Battery(Context context) {
        this.mContext = context;
        this.value = "0";
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public String getValue() {
        if (mContext == null) {
            L.e(TAG, "You must setContext() before getValue()");
            return "null";
        }
        value = batteryLevel(mContext);
        return value;
    }

    private String batteryLevel(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = intent != null ? intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) : 0;
        int scale = intent != null ? intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100) : 0;
        int percent = (level * 100) / scale;
        return String.valueOf(percent) + "%";
    }

    public String getName() {
        return name;
    }
}
