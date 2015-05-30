package com.devicehive.sspasov.device.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DevicePreferences {
	private static final String TAG = DevicePreferences.class.getSimpleName();

	private final static String NAMESPACE = "devicehive.";

	private final Context context;
	private final SharedPreferences preferences;

    public DevicePreferences(final Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(context.getPackageName()+"_devicehiveprefs", Context.MODE_PRIVATE);
    }
	
	/**preference for server url*/
    private final static String KEY_SERVER_URL = NAMESPACE.concat(".KEY_SERVER_URL");

	public String getServerUrl() {
		return preferences.getString(KEY_SERVER_URL, null);
	}
	
	public void setServerUrlSync(String serverUrl) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(KEY_SERVER_URL, serverUrl);
		editor.commit();
	}

    /**preference for device timeout*/
    private final static String KEY_DEVICE_TIMEOUT = NAMESPACE.concat(".KEY_DEVICE_TIMEOUT");

    public int getDeviceTimeout() {
        return preferences.getInt(KEY_DEVICE_TIMEOUT, -1);
    }

    public void setDeviceTimeout(int deviceTimeout) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_DEVICE_TIMEOUT, deviceTimeout);
        editor.commit();
    }

    /**preference for running device commands async*/
    private final static String KEY_ASYNC_COMMAND_EXECUTION = NAMESPACE.concat(".KEY_ASYNC_COMMAND_EXECUTION");

    public boolean getDeviceAsyncCommandExecution() {
        return preferences.getBoolean(KEY_ASYNC_COMMAND_EXECUTION, DeviceHiveConfig.DEFAULT_DEVICE_ASYNC_COMMAND_EXECUTION);
    }

    public void setDeviceAsyncCommandExecution(boolean deviceAsyncCommandExecution) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_ASYNC_COMMAND_EXECUTION, deviceAsyncCommandExecution);
        editor.commit();
    }

    /**preference for is permanent device*/
    private final static String KEY_IS_PERMANENT = NAMESPACE.concat(".KEY_IS_PERMANENT");

    public boolean getDeviceIsPermanent() {
        return preferences.getBoolean(KEY_IS_PERMANENT, DeviceHiveConfig.DEFAULT_DEVICE_IS_PERMANENT);
    }

    public void setDeviceIsPermanent(boolean isPermanent) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_PERMANENT, isPermanent);
        editor.commit();
    }
}
