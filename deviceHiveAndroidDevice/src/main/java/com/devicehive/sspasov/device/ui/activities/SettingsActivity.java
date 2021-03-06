package com.devicehive.sspasov.device.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;

public class SettingsActivity extends PreferenceActivity {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = SettingsActivity.class.getSimpleName();

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private DevicePreferences prefs;

    // ---------------------------------------------------------------------------------------------
    // Activity life cycle
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        addPreferencesFromResource(R.xml.preference);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.view_toolbar, root, false);
        toolbar.setTitle(getString(R.string.action_settings));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        root.addView(toolbar, 0); // insert at top

        prefs = new DevicePreferences(this);
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------
    private boolean isApiChanged() {
        if (DeviceConfig.API_ENDPOINT.equals(prefs.getServerUrl())) {
            return false;
        }
        return true;
    }

    private void updatePreferences() {
        L.d(TAG, "updatePreferences()");

        DeviceConfig.API_ENDPOINT = prefs.getServerUrl();
        L.d(TAG, "DeviceConfig.API_ENDPOINT: " + DeviceConfig.API_ENDPOINT);

        DeviceConfig.NETWORK_NAME = prefs.getNetworkName();
        L.d(TAG, "DeviceConfig.NETWORK_NAME: " + DeviceConfig.NETWORK_NAME);

        DeviceConfig.NETWORK_DESCRIPTION = prefs.getNetworkDescription();
        L.d(TAG, "DeviceConfig.NETWORK_DESCRIPTION: " + DeviceConfig.NETWORK_DESCRIPTION);

        DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION = prefs.getDeviceAsyncCommandExecution();
        L.d(TAG, "DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION: " + DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION);

        DeviceConfig.DEVICE_IS_PERMANENT = prefs.getDeviceIsPermanent();
        L.d(TAG, "DeviceConfig.DEVICE_IS_PERMANENT: " + DeviceConfig.DEVICE_IS_PERMANENT);

        DeviceConfig.DEVICE_TIMEOUT = prefs.getDeviceTimeout();
        L.d(TAG, "DeviceConfig.DEVICE_TIMEOUT: " + DeviceConfig.DEVICE_TIMEOUT);

        DeviceConfig.FIRST_STARTUP = prefs.isFirstStartup();
        L.d(TAG, "DeviceConfig.FIRST_STARTUP: " + DeviceConfig.FIRST_STARTUP);

    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        if (isApiChanged()) {
            updatePreferences();
            setResult(Activity.RESULT_OK, new Intent().putExtra(DeviceActivity.API_CHANGED, true));
        } else {
            updatePreferences();
            setResult(Activity.RESULT_OK);
        }
        finish();
    }
}
