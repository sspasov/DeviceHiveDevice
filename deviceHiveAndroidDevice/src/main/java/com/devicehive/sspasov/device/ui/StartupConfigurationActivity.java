package com.devicehive.sspasov.device.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;
import com.github.clans.fab.FloatingActionButton;

public class StartupConfigurationActivity extends Activity implements View.OnClickListener {

    private static final String TAG = StartupConfigurationActivity.class.getSimpleName();

    private EditText etApiEndpoint;
    private FloatingActionButton btnContinue;

    private DevicePreferences prefs;

    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        setContentView(R.layout.activity_startup_configuration);

        etApiEndpoint = (EditText) findViewById(R.id.et_startup_api_endpoint);

        btnContinue = (FloatingActionButton) findViewById(R.id.btn_startup_continue);
        btnContinue.setOnClickListener(this);

        prefs = new DevicePreferences(this);

        //TODO: DEBUG ONLY
        if (L.isUsingDebugData()) {
            etApiEndpoint.setText("http://nn8170.pg.devicehive.com/api");
        }
    }

    @Override
    public void onClick(View v) {
        etApiEndpoint.setError(null);

        isEmpty = false;

        if (etApiEndpoint.getText().toString().isEmpty()) {
            etApiEndpoint.setError(getString(R.string.empty_api_endpoint));
            isEmpty = true;
        }

        if (!isEmpty) {
            prefs.setServerUrlSync(etApiEndpoint.getText().toString());
            DeviceConfig.API_ENDPOINT = prefs.getServerUrl();

            Intent deviceActivity = new Intent(this, NetworkConfigurationActivity.class);
            startActivity(deviceActivity);
            finish();
        }
    }
}
