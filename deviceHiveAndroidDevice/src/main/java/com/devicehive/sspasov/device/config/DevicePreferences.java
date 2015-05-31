package com.devicehive.sspasov.device.config;

import android.content.Context;
import android.content.SharedPreferences;

public class DevicePreferences {
	private static final String TAG = DevicePreferences.class.getSimpleName();

	private final static String NAMESPACE = "";

	private final Context context;
	private final SharedPreferences preferences;

    public DevicePreferences(final Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
    }
	
	/**preference for server url*/
    private final static String KEY_SERVER_URL = NAMESPACE.concat("KEY_SERVER_URL");

	public String getServerUrl() {
		return preferences.getString(KEY_SERVER_URL, null);
	}
	
	public void setServerUrlSync(String serverUrl) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(KEY_SERVER_URL, serverUrl);
		editor.commit();
	}

    /**preference for device timeout*/
    private final static String KEY_DEVICE_TIMEOUT = NAMESPACE.concat("KEY_DEVICE_TIMEOUT");

    public int getDeviceTimeout() {
        return preferences.getInt(KEY_DEVICE_TIMEOUT, -1);
    }

    public void setDeviceTimeout(int deviceTimeout) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_DEVICE_TIMEOUT, deviceTimeout);
        editor.commit();
    }

    /**preference for running device commands async*/
    private final static String KEY_ASYNC_COMMAND_EXECUTION = NAMESPACE.concat("KEY_ASYNC_COMMAND_EXECUTION");

    public boolean getDeviceAsyncCommandExecution() {
        return preferences.getBoolean(KEY_ASYNC_COMMAND_EXECUTION, DeviceHiveConfig.DEFAULT_DEVICE_ASYNC_COMMAND_EXECUTION);
    }

    public void setDeviceAsyncCommandExecution(boolean deviceAsyncCommandExecution) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_ASYNC_COMMAND_EXECUTION, deviceAsyncCommandExecution);
        editor.apply();
    }

    /**preference for is permanent device*/
    private final static String KEY_IS_PERMANENT = NAMESPACE.concat("KEY_IS_PERMANENT");

    public boolean getDeviceIsPermanent() {
        return preferences.getBoolean(KEY_IS_PERMANENT, DeviceHiveConfig.DEFAULT_DEVICE_IS_PERMANENT);
    }

    public void setDeviceIsPermanent(boolean isPermanent) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_PERMANENT, isPermanent);
        editor.apply();
    }

    /**preference for first time startup configuration*/
    private final static String KEY_FIRST_STARTUP = NAMESPACE.concat("KEY_FIRST_STARTUP");

    public boolean isFirstStartup() {
        return preferences.getBoolean(KEY_FIRST_STARTUP, DeviceHiveConfig.DEFAULT_FIRST_STARTUP);
    }

    public void setIsFirstStartup(boolean isFirstStartup) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_STARTUP, isFirstStartup);
        editor.apply();
    }

    /**preference for is network name and description*/
    private final static String KEY_NETWORK_NAME = NAMESPACE.concat("KEY_NETWORK_NAME");

    public String getNetworkName() {
        return preferences.getString(KEY_NETWORK_NAME, null);
    }

    public void setNetworkName(String networkName) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NETWORK_NAME, networkName);
        editor.apply();
    }

    private final static String KEY_NETWORK_DESCRIPTION = NAMESPACE.concat("KEY_NETWORK_DESCRIPTION");

    public String getNetworkDescription() {
        return preferences.getString(KEY_NETWORK_DESCRIPTION, null);
    }

    public void setNetworkDescription(String networkDescription) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NETWORK_DESCRIPTION, networkDescription);
        editor.apply();
    }


}
