package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.provider.Settings;
import android.util.Log;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Equipment;
import com.dataart.android.devicehive.device.EquipmentNotification;

import java.util.HashMap;

public class TestEquipment extends Equipment {

	private static final String TAG = TestEquipment.class.getSimpleName();

	private Context mContext;
    private Sensor mSensor;

    private SensorEventListener sensorListener;

    private String equipmentName;
    private String equipmentCode;
    private int equipmentType;
    private boolean multipleValueEquipment;
	private float x, y, z;


	public TestEquipment(Context context, Sensor sensor) {
        super(equipmentData(
                        sensor.getName(),
                        Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_" + sensor.getType(),
                        sensor.getType() + "")
        );
        Log.d(TAG, "Attaching equipment: " + sensor.getName());
        this.mContext = context;
		mSensor = sensor;

        equipmentName = mSensor.getName();
        equipmentCode = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+"_"+ mSensor.getType();
        equipmentType = mSensor.getType();

        multipleValueEquipment = isMultipleValueEquipment();

        createSensorListener();
    }

    private boolean isMultipleValueEquipment() {
        return equipmentType != 2 && equipmentType != 14 && equipmentType != 5
                && equipmentType != 6 && equipmentType != 8 && equipmentType != 18
                && equipmentType != 19;
    }

    private void createSensorListener() {
        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                x = event.values[0];
                //Log.d(TAG, mSensor.getName()+" value x: "+x);

                if(multipleValueEquipment) {
                    //Log.d(TAG, equipmentName);
                    y = event.values[1];
                    //Log.d(TAG, mSensor.getName()+" value y: "+y);

                    z = event.values[2];
                    //Log.d(TAG, mSensor.getName() + " value z: " + z);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
    }

    public TestEquipment(String name, String code, String type) {
		super(equipmentData(name, code, type));
	}

    /**
     * @name - simple equipment name
     * @code - unique ID for the equipment. It's used to point and use this equipment.
     * @type - equipment type **/
	private static EquipmentData equipmentData(String name, String code, String type) {
        return new EquipmentData(name, code, type);
	}

    public SensorEventListener getSensorListener() {
        return sensorListener;
    }

    public Sensor getSensor() {
        return mSensor;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

	@Override
	public void onBeforeRunCommand(Command command) {
		Log.d(TAG, "onBeforeRunCommand: " + command.getCommand());
	}

	@Override
	public boolean shouldRunCommandAsynchronously(final Command command) {
		return false;
	}

	@Override
	public CommandResult runCommand(final Command command) {
		Log.d(TAG, "runCommand: " + command.getCommand());

        HashMap<String, Object> sensorParam = new HashMap<>();

        Parameter p = new Parameter("x", x+"");
        sensorParam.put(p.name, p.value);
        Log.d(TAG, mSensor.getName() + " value x: " + x);

        if(multipleValueEquipment) {
            p = new Parameter("y", y+"");
            sensorParam.put(p.name, p.value);
            Log.d(TAG, mSensor.getName() + " value y: " + y);

            p = new Parameter("z", z+"");
            sensorParam.put(p.name, p.value);
            Log.d(TAG, mSensor.getName() + " value z: " + z);
        }

		HashMap commandParam = (HashMap) command.getParameters();
		Log.d(TAG, "Executing Command on Equipment (code): " + commandParam.get("equipment"));

        sendNotification(new EquipmentNotification(equipmentCode, sensorParam));

		return new CommandResult(
                CommandResult.STATUS_COMLETED,
				"Executed on "+equipmentName+"!");
	}


	@Override
	protected boolean onRegisterEquipment() {
		Log.d(TAG, "onRegisterEquipment");
		return true;
	}

	@Override
    protected boolean onUnregisterEquipment() {
		Log.d(TAG, "onUnregisterEquipment");
		return true;
	}

	@Override
	protected void onStartProcessingCommands() {
		Log.d(TAG, "onStartProcessingCommands");
	}

	@Override
	protected void onStopProcessingCommands() {
		/*HashMap<String, Object> parameters = new HashMap<String, Object>();
		Parameter p = new Parameter("result", "23");

		parameters.put(p.name, p.value);
		sendNotification(new EquipmentNotification(getEquipmentData().getCode() ,parameters));*/
		Log.d(TAG, "onStopProcessingCommands");
	}
}
