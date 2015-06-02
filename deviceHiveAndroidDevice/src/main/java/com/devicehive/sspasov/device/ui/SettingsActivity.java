package com.devicehive.sspasov.device.ui;

import android.app.Activity;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;

public class SettingsActivity extends SherlockPreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private DevicePreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        addPreferencesFromResource(R.xml.preference);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = new DevicePreferences(SettingsActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            updatePreferences();
            setResult(Activity.RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePreferences() {
        L.d(TAG, "updatePreferences()");
        DeviceConfig.API_ENDPOINT = prefs.getServerUrl();
        DeviceConfig.NETWORK_NAME = prefs.getNetworkName();
        DeviceConfig.NETWORK_DESCRIPTION = prefs.getNetworkDescription();
        DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION = prefs.getDeviceAsyncCommandExecution();
        DeviceConfig.DEVICE_IS_PERMANENT = prefs.getDeviceIsPermanent();
        DeviceConfig.DEVICE_TIMEOUT = prefs.getDeviceTimeout();
        DeviceConfig.FIRST_STARTUP = prefs.isFirstStartup();
    }

    @Override
    public void onBackPressed() {
        updatePreferences();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
