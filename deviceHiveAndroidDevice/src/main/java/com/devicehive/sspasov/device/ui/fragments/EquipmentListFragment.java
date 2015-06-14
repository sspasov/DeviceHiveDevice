package com.devicehive.sspasov.device.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.dataart.android.devicehive.EquipmentData;
import com.devicehive.sspasov.device.adapters.EquipmentAdapter;

import java.util.List;

public class EquipmentListFragment extends ListFragment {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static String TAG = EquipmentListFragment.class.getSimpleName();

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private List<EquipmentData> equipment;
    private EquipmentAdapter equipmentAdapter;

    private List<Sensor> deviceSensors;

    private static EquipmentListFragment instance;

    // ---------------------------------------------------------------------------------------------
    // Activity life cycle
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (equipment != null) {
            equipmentAdapter = new EquipmentAdapter(activity, equipment);
            setListAdapter(equipmentAdapter);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public static EquipmentListFragment getInstance() {
        if (instance == null) {
            instance = new EquipmentListFragment();
        }
        return instance;
    }

    public static EquipmentListFragment newInstance() {
        instance = new EquipmentListFragment();
        return instance;
    }

    public void setEquipment(List<EquipmentData> equipment) {
        this.equipment = equipment;
        if (getActivity() != null) {
            equipmentAdapter = new EquipmentAdapter(getActivity(), this.equipment);
            setListAdapter(equipmentAdapter);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
}