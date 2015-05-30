package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by toni on 29.05.15.
 */
public class DeviceTimeOn {

    private static final String TAG = DeviceTimeOn.class.getSimpleName();
    private final String name = "value";
    private String value;
    private Context mContext;

    private static DeviceTimeOn instance;

    public static DeviceTimeOn getInstance() {
        if(instance == null) {
            instance = new DeviceTimeOn();
        }
        return instance;
    }

    public static DeviceTimeOn getInstance(Context context) {
        if(instance == null) {
            instance = new DeviceTimeOn(context);
        }
        return instance;
    }

    public DeviceTimeOn() {}


    public DeviceTimeOn(Context context) {
        this.mContext = context;
        this.value = "0";
    }

    public void setContext(Context context) {
        this.mContext = context;

    }

    public String getValue() {
        if(mContext == null) {
            Log.e(TAG, "You must setContext() before getValue");
            return "null";
        }
        return getTimeOn(mContext);
    }

    private String getTimeOn(Context context) {
        long millis = SystemClock.elapsedRealtime();
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return String.valueOf(hms);
    }

    public String getName() {
        return name;
    }
}

