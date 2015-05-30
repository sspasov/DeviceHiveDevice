package com.devicehive.sspasov.device;

import android.app.Application;
import android.util.Log;

import com.devicehive.sspasov.device.objects.TestDevice;
import com.devicehive.sspasov.device.utils.DeviceConfig;
import com.devicehive.sspasov.device.utils.DeviceHiveConfig;
import com.devicehive.sspasov.device.utils.DevicePreferences;

public class DeviceApplication extends Application {

    private static final String TAG = DeviceApplication.class.getSimpleName();

	private TestDevice device;
	
	@Override
	public void onCreate() {
		super.onCreate();
		device = new TestDevice(getApplicationContext());
        loadPreferences();
	}

    private void loadPreferences() {
        device.setDebugLoggingEnabled(BuildConfig.DEBUG);

        final DevicePreferences prefs = new DevicePreferences(this);
        String serverUrl = prefs.getServerUrl();
        if (serverUrl == null) {
            serverUrl = DeviceHiveConfig.API_ENDPOINT;
            prefs.setServerUrlSync(serverUrl);
        }
        Log.d(TAG, "DevicePref apiendpoint: "+serverUrl);
        device.setApiEnpointUrl(serverUrl);

        int deviceTimeout = prefs.getDeviceTimeout();
        if( deviceTimeout == -1 ) {
            deviceTimeout = DeviceHiveConfig.DEFAULT_DEVICE_TIMEOUT;
            prefs.setDeviceTimeout(deviceTimeout);
        }
        Log.d(TAG, "DevicePref device timeout: "+deviceTimeout);
        DeviceConfig.DEVICE_TIMEOUT = deviceTimeout;

        boolean deviceAsyncCommandExecution = prefs.getDeviceAsyncCommandExecution();
        Log.d(TAG, "DevicePref device async cmds: "+deviceAsyncCommandExecution);
        DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION = deviceAsyncCommandExecution;

        boolean deviceIsPermanent = prefs.getDeviceIsPermanent();
        Log.d(TAG, "DevicePref device is permanent: "+deviceIsPermanent);
        DeviceConfig.DEVICE_IS_PERMANENT = deviceIsPermanent;
    }

    public TestDevice getDevice() {
		return device;
	}
	
}
