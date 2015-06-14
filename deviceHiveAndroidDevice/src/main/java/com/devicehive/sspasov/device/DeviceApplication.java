package com.devicehive.sspasov.device;

import android.app.Application;

import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DeviceHiveConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;

public class DeviceApplication extends Application {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = DeviceApplication.class.getSimpleName();

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private DevicePreferences prefs;

    // ---------------------------------------------------------------------------------------------
    // Activity life cycle
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        L.useDebugMode(false);
        L.useDebugData(false);
        loadPreferences();
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------
    private void loadPreferences() {
        L.d(TAG, "loadPreferences()");

        prefs = new DevicePreferences(this);


        L.d(TAG, "DevicePref device is first time startup: " + prefs.isFirstStartup());
        DeviceConfig.FIRST_STARTUP = prefs.isFirstStartup();


        if (prefs.getServerUrl() == null) {
            prefs.setServerUrlSync(DeviceHiveConfig.DEFAULT_API_ENDPOINT);
        }
        L.d(TAG, "DevicePref apiendpoint: " + prefs.getServerUrl());
        DeviceConfig.API_ENDPOINT = prefs.getServerUrl();


        if (prefs.getDeviceTimeout() == -1) {
            prefs.setDeviceTimeout(DeviceHiveConfig.DEFAULT_DEVICE_TIMEOUT);
        }
        L.d(TAG, "DevicePref device timeout: " + prefs.getDeviceTimeout());
        DeviceConfig.DEVICE_TIMEOUT = prefs.getDeviceTimeout();


        L.d(TAG, "DevicePref device async cmds: " + prefs.getDeviceAsyncCommandExecution());
        DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION = prefs.getDeviceAsyncCommandExecution();


        L.d(TAG, "DevicePref device is permanent: " + prefs.getDeviceIsPermanent());
        DeviceConfig.DEVICE_IS_PERMANENT = prefs.getDeviceIsPermanent();


        if (prefs.getNetworkName() == null) {
            prefs.setNetworkName(DeviceHiveConfig.DEFAULT_NETWORK_NAME);
        }
        L.d(TAG, "DevicePref device network name: " + prefs.getNetworkName());
        DeviceConfig.NETWORK_NAME = prefs.getNetworkName();


        if (prefs.getNetworkDescription() == null) {
            prefs.setNetworkDescription(DeviceHiveConfig.DEFAULT_NETWORK_DESCRIPTION);
        }
        L.d(TAG, "DevicePref device network description: " + prefs.getNetworkDescription());
        DeviceConfig.NETWORK_DESCRIPTION = prefs.getNetworkDescription();

    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
}
