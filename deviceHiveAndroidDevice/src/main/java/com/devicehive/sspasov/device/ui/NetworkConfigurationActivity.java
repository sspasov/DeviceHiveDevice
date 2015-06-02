package com.devicehive.sspasov.device.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dataart.android.devicehive.Network;
import com.dataart.android.devicehive.client.commands.DeviceClientCommand;
import com.dataart.android.devicehive.client.commands.GetNetworksCommand;
import com.dataart.android.devicehive.network.DeviceHiveResultReceiver;
import com.dataart.android.devicehive.network.NetworkCommand;
import com.dataart.android.devicehive.network.NetworkCommandConfig;
import com.devicehive.sspasov.device.BuildConfig;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.config.DevicePreferences;
import com.devicehive.sspasov.device.utils.L;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NetworkConfigurationActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final static String TAG = NetworkConfigurationActivity.class.getSimpleName();

    private final String USERNAME = "getAvailableNetworks";
    private final String PASSWORD = "getAvailableNetworks";

    private EditText etNetworkName;
    private String networkName;

    private EditText etNetworkDescription;
    private String networkDescription;

    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private List<Network> networks;
    private ArrayList<String> simpleNetworkList;
    private Button btnContinue;

    private DevicePreferences prefs;

    private boolean isEmptyNetworkName;
    private boolean isEmptyNetworkDescription;
    private boolean isCreatingNewNetwork;

    private DeviceHiveResultReceiver resultReceiver = null;

    private final DeviceHiveResultReceiver.ResultListener resultListener = new DeviceHiveResultReceiver.ResultListener() {
        @Override
        public void onReceiveResult(int code, int tag, Bundle data) {
            NetworkConfigurationActivity.this.onReceiveResult(code, tag, data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_configuration);

        simpleNetworkList = new ArrayList<>();

        etNetworkName = (EditText) findViewById(R.id.et_startup_network_name);
        etNetworkDescription = (EditText) findViewById(R.id.et_startup_network_description);
        etNetworkDescription.setHint("(Optional)");

        btnContinue = (Button) findViewById(R.id.btn_network_continue);
        btnContinue.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.sp_existing_networks);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, simpleNetworkList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.post(new Runnable() {
            @Override
            public void run() {
                startNetworksRequest();
            }
        });
        spinner.setOnItemSelectedListener(this);

        prefs = new DevicePreferences(this);

        //TODO: DEBUG ONLY
        if (L.isUsingDebugData()) {
            etNetworkName.setText("test network");
            etNetworkDescription.setText("test network description");
        }
    }

    @Override
    public void onClick(View v) {
        etNetworkName.setError(null);
        etNetworkDescription.setError(null);

        if (isCreatingNewNetwork) {
            if (etNetworkName.getText().toString().isEmpty()) {
                etNetworkName.setError("You must enter Network name.");
                isEmptyNetworkName = true;
            } else {
                isEmptyNetworkName = false;
            }

            if (etNetworkDescription.getText().toString().isEmpty()) {
                etNetworkDescription.setError("You must enter Network description.");
                isEmptyNetworkDescription = true;
            } else {
                isEmptyNetworkDescription = false;
            }

            if (!isEmptyNetworkName && !isEmptyNetworkDescription) {
                prefs.setIsFirstStartup(false);
                DeviceConfig.FIRST_STARTUP = prefs.isFirstStartup();

                prefs.setNetworkName(etNetworkName.getText().toString());
                DeviceConfig.NETWORK_NAME = prefs.getNetworkName();

                prefs.setNetworkDescription(etNetworkDescription.getText().toString());
                DeviceConfig.NETWORK_DESCRIPTION = prefs.getNetworkDescription();

                Intent deviceActivity = new Intent(this, DeviceActivity.class);
                startActivity(deviceActivity);
                finish();
            }
        } else {
            prefs.setIsFirstStartup(false);
            DeviceConfig.FIRST_STARTUP = prefs.isFirstStartup();

            prefs.setNetworkName(networkName);
            DeviceConfig.NETWORK_NAME = prefs.getNetworkName();

            prefs.setNetworkDescription(networkDescription);
            DeviceConfig.NETWORK_DESCRIPTION = prefs.getNetworkDescription();

            Intent deviceActivity = new Intent(this, DeviceActivity.class);
            startActivity(deviceActivity);
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            isCreatingNewNetwork = true;
        } else {
            //TODO: FIX THE CRASH, selectng last one crashes
            isCreatingNewNetwork = false;
            networkName = networks.get(position).getName();
            networkDescription = networks.get(position).getDescription();
            L.d(TAG, networkName);
            L.d(TAG, networkDescription);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        isCreatingNewNetwork = true;
    }

    private void startNetworksRequest() {
        startCommand(new GetNetworksCommand());
    }

    protected final <T extends NetworkCommand> void startCommand(final T command) {
        command.start(getApplicationContext(), getNetworkCommandConfig());
    }

    protected NetworkCommandConfig getNetworkCommandConfig() {
        final NetworkCommandConfig config = new NetworkCommandConfig(DeviceConfig.API_ENDPOINT,
                getResultReceiver(), BuildConfig.DEBUG);

        config.setBasicAuthorisation(USERNAME, PASSWORD);
        return config;
    }

    protected DeviceHiveResultReceiver getResultReceiver() {
        if (null == resultReceiver) {
            resultReceiver = new DeviceHiveResultReceiver();
            resultReceiver.setResultListener(resultListener, true);
        }
        return resultReceiver;
    }

    private static final int TAG_GET_NETWORKS = getTagId(GetNetworksCommand.class);

    protected void onReceiveResult(final int resultCode, final int tagId, final Bundle resultData) {
        switch (resultCode) {
            case DeviceHiveResultReceiver.MSG_COMPLETE_REQUEST:
                break;
            case DeviceHiveResultReceiver.MSG_EXCEPTION:
                final Throwable exception = DeviceClientCommand.getThrowable(resultData);
                L.e(TAG, "Failed to execute network command", exception);
                break;
            case DeviceHiveResultReceiver.MSG_STATUS_FAILURE:
                int statusCode = DeviceClientCommand.getStatusCode(resultData);
                L.e(TAG, "Failed to execute network command. Status code: " + statusCode);
                break;
            case DeviceHiveResultReceiver.MSG_HANDLED_RESPONSE:
                if (tagId == TAG_GET_NETWORKS) {
                    networks = GetNetworksCommand.getNetworks(resultData);
                    L.d(TAG, "Fetched networks: " + networks);
                    if (networks != null) {
                        Collections.sort(networks, new Comparator<Network>() {
                            @Override
                            public int compare(Network lhs, Network rhs) {
                                return lhs.getName().compareToIgnoreCase(
                                        rhs.getName());
                            }
                        });

                        simpleNetworkList.add("New network");
                        for (int i = 0; i < networks.size(); i++) {
                            simpleNetworkList.add(networks.get(i).getName());
                        }
                        adapter.notifyDataSetChanged();
                        spinner.setAdapter(adapter);
                    }
                }

                break;
        }
    }

    protected static int getTagId(final Class<?> tag) {
        return getTagId(tag.getName());
    }

    protected static int getTagId(final String tag) {
        return DeviceHiveResultReceiver.getIdForTag(tag);
    }


}
