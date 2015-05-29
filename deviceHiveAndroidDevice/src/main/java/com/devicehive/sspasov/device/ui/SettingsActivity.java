package com.devicehive.sspasov.device.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.utils.DevicePreferences;

public class SettingsActivity extends SherlockActivity {

	private EditText serverUrlEdit;

	private DevicePreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		serverUrlEdit = (EditText) findViewById(R.id.server_url_edit);

		prefs = new DevicePreferences(SettingsActivity.this);

		findViewById(R.id.undo_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						resetValues();
					}
				});
		findViewById(R.id.save_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						saveValues();
					}
				});

		resetValues();
	}

	private void resetValues() {
		serverUrlEdit.setText(prefs.getServerUrl());
	}

	private void saveValues() {
		final String serverUrl = serverUrlEdit.getText().toString();
		if (TextUtils.isEmpty(serverUrl)) {
			serverUrlEdit.setError("Server URL is required");
		} else {
			if (!serverUrl.equals(prefs.getServerUrl())) {
				prefs.setServerUrlSync(serverUrl);
				setResult(RESULT_OK);
			}
			finish();
		}
	}
}
