package com.devicehive.sspasov.device.ui;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.utils.DevicePreferences;

public class SettingsActivity extends SherlockPreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

	private DevicePreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = new DevicePreferences(SettingsActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
           finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
