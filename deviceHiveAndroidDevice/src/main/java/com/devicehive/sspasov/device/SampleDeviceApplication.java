package com.devicehive.sspasov.device;

import android.app.Application;

import com.devicehive.sspasov.device.objects.TestDevice;
import com.devicehive.sspasov.device.utils.DeviceHiveConfig;
import com.devicehive.sspasov.device.utils.SampleDevicePreferences;

public class SampleDeviceApplication extends Application {

	private TestDevice device;
	
	@Override
	public void onCreate() {
		super.onCreate();
		device = new TestDevice(getApplicationContext());
		device.setDebugLoggingEnabled(BuildConfig.DEBUG);
		
		final SampleDevicePreferences prefs = new SampleDevicePreferences(this);
		String serverUrl = prefs.getServerUrl();
		if (serverUrl == null) {
			serverUrl = DeviceHiveConfig.API_ENDPOINT;
			prefs.setServerUrlSync(serverUrl);
		} 
		device.setApiEnpointUrl(serverUrl);
	}
	
	public TestDevice getDevice() {
		return device;
	}
	
}
