package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.util.Log;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Equipment;

import java.util.HashMap;

public class TestEquipment extends Equipment {

	private static final String TAG = TestEquipment.class.getSimpleName();

	private Context mContext;
	private Sensor mSensor;
	private SensorManager mSensorManager;


	public TestEquipment(Context context, Sensor sensor, SensorManager sensorManager) {
        super(equipmentData(
                sensor.getName(),
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+"_"+ sensor.getType(),
                sensor.getType()+""));
        this.mContext = context;
		this.mSensor = sensor;
		this.mSensorManager = sensorManager;
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
		
		// run command

		HashMap param = (HashMap) command.getParameters();
		Log.d(TAG, "Executing Command on Equipment: " + param.get("equipment"));

		return new CommandResult(CommandResult.STATUS_COMLETED,
				"Executed on Android test equipment!");
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
