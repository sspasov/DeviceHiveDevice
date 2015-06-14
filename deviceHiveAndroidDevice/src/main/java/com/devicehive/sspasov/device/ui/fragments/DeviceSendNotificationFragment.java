package com.devicehive.sspasov.device.ui.fragments;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.Notification;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.adapters.ParametersAdapter;
import com.devicehive.sspasov.device.objects.Parameter;
import com.devicehive.sspasov.device.utils.L;
import com.github.clans.fab.FloatingActionButton;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DeviceSendNotificationFragment extends Fragment {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = "DeviceSendNotificationFragment";

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private FloatingActionButton btnSendNotification;

    private TextView etNotificationName;
    private Spinner equipmentSpinner;
    private ListView lvParameters;

    private NotificationSender notificationSender;
    private ParameterProvider parameterProvider;
    private ParametersAdapter parametersAdapter;
    private List<EquipmentData> equipment;
    private List<Parameter> parameters = new LinkedList<>();
    private String mDeviceStatus;

    private static DeviceSendNotificationFragment instance;

    // ---------------------------------------------------------------------------------------------
    // Interfaces
    // ---------------------------------------------------------------------------------------------
    public interface NotificationSender {
        void sendNotification(Notification notification);
    }

    public interface ParameterProvider {
        void queryParameter();
    }

    // ---------------------------------------------------------------------------------------------
    // Activity life cycle
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        notificationSender = (NotificationSender) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupEquipmentSpinner(equipment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_notification, container, false);

        etNotificationName = (EditText) rootView.findViewById(R.id.et_notification_name);

        equipmentSpinner = (Spinner) rootView.findViewById(R.id.equipment_spinner);
        equipmentSpinner.setPrompt("Select equipment");
        setupEquipmentSpinner(equipment);

        lvParameters = (ListView) rootView.findViewById(R.id.lv_parameters);
        addParameter("Add extra parameter", "");
        lvParameters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (parameterProvider != null) {
                        parameterProvider.queryParameter();
                    }
                }
            }
        });
        lvParameters.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    parametersAdapter.removeParameter(position);
                    parametersAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        btnSendNotification = (FloatingActionButton) rootView.findViewById(R.id.btn_send_notification);
        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDeviceStatus.contains("Offline")) {
                    sendNotification();
                    clearView();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Warning !!! Device is offline.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        btnSendNotification = null;
        etNotificationName = null;
        equipmentSpinner = null;
        super.onDestroyView();
    }

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public static DeviceSendNotificationFragment getInstance() {
        if (instance == null) {
            instance = new DeviceSendNotificationFragment();
        }
        return instance;
    }

    public static DeviceSendNotificationFragment newInstance() {
        instance = new DeviceSendNotificationFragment();
        return instance;
    }

    public void setNotificationSender(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void setParameterProvider(ParameterProvider parameterProvider) {
        this.parameterProvider = parameterProvider;
    }

    public void setEquipment(List<EquipmentData> equipment) {
        this.equipment = equipment;
        setupEquipmentSpinner(equipment);
    }

    public void setDeviceStatus(String deviceStatus) {
        L.d(TAG, "setDeviceStatus()");
        this.mDeviceStatus = deviceStatus;
    }

    public void addParameter(String name, String value) {
        this.parameters.add(new Parameter(name, value));
        setupParameters(this.parameters);
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------
    private void setupEquipmentSpinner(List<EquipmentData> equipment) {
        if (equipment != null && equipmentSpinner != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this.getActivity(),
                    android.R.layout.simple_spinner_item,
                    getEquipmentItems(equipment));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            equipmentSpinner.setAdapter(adapter);
        }
    }

    private List<String> getEquipmentItems(List<EquipmentData> equipment) {
        final List<String> equipmentNames = new LinkedList<String>();
        equipmentNames.add("None");
        for (EquipmentData eq : equipment) {
            equipmentNames.add(eq.getName());
        }
        return equipmentNames;
    }

    private void setupParameters(List<Parameter> parameters) {
        L.d(TAG, "setupParameters()");
        parametersAdapter = new ParametersAdapter(getActivity(), parameters);
        parametersAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setupParameters(DeviceSendNotificationFragment.this.parameters);
            }
        });
        lvParameters.setAdapter(parametersAdapter);
    }

    private void sendNotification() {
        String notification = etNotificationName.getText().toString();
        if (TextUtils.isEmpty(notification)) {
            notification = "TestNotificationAndroidFramework";
        }

        HashMap<String, Object> parameters = paramsAsMap(this.parameters);

        int selectedItemPosition = equipmentSpinner.getSelectedItemPosition();
        if (selectedItemPosition != 0) {
            final EquipmentData selectedEquipment = equipment.get(selectedItemPosition - 1);
            parameters.put("equipment", selectedEquipment.getCode());
        }

        if (notificationSender != null) {
            notificationSender.sendNotification(new Notification(notification, parameters));
        }
    }

    private void clearView() {
        etNotificationName.setText("");
        equipmentSpinner.refreshDrawableState();
        for (int i = 1; i < parametersAdapter.getCount(); i++) {
            parametersAdapter.removeParameter(i);
        }
        lvParameters.setAdapter(parametersAdapter);
    }

    private static HashMap<String, Object> paramsAsMap(List<Parameter> params) {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        for (Parameter param : params) {
            paramsMap.put(param.name, param.value);
        }
        return paramsMap;
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------

}
