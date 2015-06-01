package com.devicehive.sspasov.device.objects;

import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

/**
 * Created by toni on 29.05.15.
 */
public class DeviceTimeOn {

    private static final String TAG = DeviceTimeOn.class.getSimpleName();
    private final String name = "value";
    private String value;

    private static DeviceTimeOn instance;

    public static DeviceTimeOn getInstance() {
        if(instance == null) {
            instance = new DeviceTimeOn();
        }
        return instance;
    }

    public DeviceTimeOn() {
        this.value = "0";
    }

    public String getValue() {
        value = getTimeOn();
        return value;
    }

    private String getTimeOn() {
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

