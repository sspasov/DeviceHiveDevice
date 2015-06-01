package com.devicehive.sspasov.device.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;

public class StartupConfigurationActivity extends Activity implements View.OnClickListener {

    private static final String TAG = StartupConfigurationActivity.class.getSimpleName();

    private EditText etApiEndpoint;
    private EditText etNetworkName;
    private EditText etNetworkDescription;

    private Button btnContinue;

    private DevicePreferences prefs;

    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        setContentView(R.layout.activity_startup_configuration);

        etApiEndpoint = (EditText) findViewById(R.id.et_startup_api_endpoint);
        etNetworkName = (EditText) findViewById(R.id.et_startup_network_name);
        etNetworkDescription = (EditText) findViewById(R.id.et_startup_network_description);
        etNetworkDescription.setHint("(Optional)");

        btnContinue = (Button) findViewById(R.id.btn_startup_continue);
        btnContinue.setOnClickListener(this);

        prefs = new DevicePreferences(this);

        //TODO: DEBUG ONLY
        if(L.isUsingDebugData()){
            etApiEndpoint.setText("http://nn8170.pg.devicehive.com/api");
            etNetworkName.setText("test network");
            etNetworkDescription.setText("test network description");
        }
    }

    @Override
    public void onClick(View v) {
        etApiEndpoint.setError(null);
        etNetworkName.setError(null);

        if( etApiEndpoint.getText().toString().isEmpty() ) {
            etApiEndpoint.setError("You must enter server URL.");
            isEmpty = true;
        } else {
            isEmpty = false;
        }

        //TODO: second activity with spinner only for the network
        if( etNetworkName.getText().toString().isEmpty() ) {
            etNetworkName.setError("You must enter Network name.");
            isEmpty = true;
        } else {
            isEmpty = false;
        }

        if(!isEmpty) {
            prefs.setIsFirstStartup(false);
            DeviceConfig.FIRST_STARTUP = prefs.isFirstStartup();

            prefs.setServerUrlSync(etApiEndpoint.getText().toString());
            DeviceConfig.API_ENDPOINT = prefs.getServerUrl();

            prefs.setNetworkName(etNetworkName.getText().toString());
            DeviceConfig.NETWORK_NAME = prefs.getNetworkName();

            prefs.setNetworkDescription(etNetworkDescription.getText().toString());
            DeviceConfig.NETWORK_DESCRIPTION = prefs.getNetworkDescription();

            Intent deviceActivity = new Intent(this, DeviceActivity.class);
            startActivity(deviceActivity);
            finish();
        }

    }
}
