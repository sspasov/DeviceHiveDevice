package com.devicehive.sspasov.device.ui;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;

public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private DevicePreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        addPreferencesFromResource(R.xml.preference);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        toolbar.setTitle(getString(R.string.action_settings));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePreferences();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        root.addView(toolbar, 0); // insert at top


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

    @Override
    public void onBackPressed() {
        updatePreferences();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
