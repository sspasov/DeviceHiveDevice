package com.devicehive.sspasov.device.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;
import com.github.clans.fab.FloatingActionButton;

public class StartupConfigurationActivity extends AppCompatActivity implements View.OnClickListener {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = StartupConfigurationActivity.class.getSimpleName();
    public static final String API = "api";

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private EditText etApiEndpoint;
    private FloatingActionButton btnContinue;

    private DevicePreferences prefs;

    private boolean isEmpty;

    // ---------------------------------------------------------------------------------------------
    // Activity life cycle
    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        setContentView(R.layout.activity_startup_configuration);

        setupToolbar();

        etApiEndpoint = (EditText) findViewById(R.id.et_startup_api_endpoint);

        btnContinue = (FloatingActionButton) findViewById(R.id.btn_startup_continue);
        btnContinue.setOnClickListener(this);

        prefs = new DevicePreferences(this);

        if (getIntent() != null && getIntent().hasExtra(API)) {
            etApiEndpoint.setText(getIntent().getStringExtra(API));
        }

        //TODO: DEBUG ONLY
        if (L.isUsingDebugData()) {
            etApiEndpoint.setText("http://nn8170.pg.devicehive.com/api");
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_startup_activity);
        toolbar.setTitle(getString(R.string.title_activity_configuration));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
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

            Intent networkConfigurationActivity = new Intent(this, NetworkConfigurationActivity.class);
            networkConfigurationActivity.putExtra("from", TAG);
            startActivity(networkConfigurationActivity);
            finish();
        }
    }
}
