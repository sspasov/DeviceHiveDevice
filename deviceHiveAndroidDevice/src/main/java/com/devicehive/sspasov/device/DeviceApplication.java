package com.devicehive.sspasov.device;

import android.app.Application;
import android.util.Log;

import com.devicehive.sspasov.device.utils.DeviceConfig;
import com.devicehive.sspasov.device.utils.DeviceHiveConfig;
import com.devicehive.sspasov.device.utils.DevicePreferences;

public class DeviceApplication extends Application {

    private static final String TAG = DeviceApplication.class.getSimpleName();

    DevicePreferences prefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
        loadPreferences();

	}

    private void loadPreferences() {

        prefs = new DevicePreferences(this);

        Log.d(TAG, "DevicePref device is first time startup: "+prefs.isFirstStartup());
        DeviceConfig.FIRST_STARTUP = prefs.isFirstStartup();


        if(prefs.getServerUrl() == null) {
            prefs.setServerUrlSync(DeviceHiveConfig.API_ENDPOINT);
        }
        Log.d(TAG, "DevicePref apiendpoint: "+prefs.getServerUrl());


        if(prefs.getDeviceTimeout() == -1) {
            prefs.setDeviceTimeout(DeviceHiveConfig.DEFAULT_DEVICE_TIMEOUT);
        }
        Log.d(TAG, "DevicePref device timeout: "+prefs.getDeviceTimeout());
        DeviceConfig.DEVICE_TIMEOUT = prefs.getDeviceTimeout();


        Log.d(TAG, "DevicePref device async cmds: "+prefs.getDeviceAsyncCommandExecution());
        DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION = prefs.getDeviceAsyncCommandExecution();


        Log.d(TAG, "DevicePref device is permanent: "+prefs.getDeviceIsPermanent());
        DeviceConfig.DEVICE_IS_PERMANENT = prefs.getDeviceIsPermanent();


        if(prefs.getNetworkName() == null) {
            prefs.setNetworkName(DeviceHiveConfig.DEFAULT_NETWORK_NAME);
        }
        Log.d(TAG, "DevicePref device network name: "+prefs.getNetworkName());
        DeviceConfig.NETWORK_NAME = prefs.getNetworkName();


        if(prefs.getNetworkDescription() == null) {
            prefs.setNetworkDescription(DeviceHiveConfig.DEFAULT_NETWORK_DESCRIPTION);
        }
        Log.d(TAG, "DevicePref device network description: "+prefs.getNetworkDescription());
        DeviceConfig.NETWORK_DESCRIPTION = prefs.getNetworkDescription();

    }
	
}
