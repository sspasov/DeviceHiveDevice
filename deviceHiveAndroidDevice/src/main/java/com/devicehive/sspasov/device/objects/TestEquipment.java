package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
	private static Sensor mSensor;
	private static SensorManager mSensorManager;
    private static SensorEventListener sensorListener;

    private String equipmentName;
    private String equipmentCode;
    private int equipmentType;


	public TestEquipment(Context context, Sensor sensor, SensorManager sensorManager) {
        super(equipmentData(
                sensor.getName(),
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+"_"+ sensor.getType(),
                sensor.getType()+"")
               );
        this.mContext = context;
		mSensor = sensor;
		mSensorManager = sensorManager;

        equipmentName = mSensor.getName();
        equipmentCode = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)+"_"+ mSensor.getType();
        equipmentType = mSensor.getType();


	}

	public TestEquipment(String name, String code, String type) {
		super(equipmentData(name, code, type));
	}

    /**
     * @name - simple equipment name
     * @code - unique ID for the equipment. It's used to point and use this equipment.
     * @type - equipment type **/
	private static EquipmentData equipmentData(String name, String code, String type) {
        EquipmentData equipmentData = new EquipmentData(name, code, type);
       // equipmentData.setData(data);
        return equipmentData;
	}

	private HashMap getSensorData() {
        final HashMap data = new HashMap(); //TODO: NEED FIX
        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                data.clear();
                Parameter x = new Parameter("x", sensorEvent.values[0]+"");
                Log.d(TAG, mSensor.getName()+" value x: "+x.value);
                data.put(x.name, x.value);

                Parameter y = new Parameter("y", sensorEvent.values[1]+"");
                Log.d(TAG, mSensor.getName()+" value y: "+y.value);
                data.put(x.name, y.value);

                Parameter z = new Parameter("z", sensorEvent.values[2]+"");
                Log.d(TAG, mSensor.getName()+" value z: "+z.value);
                data.put(x.name, z.value);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        if(mSensor != null) {
            if(!mSensorManager.registerListener(sensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL)){
                Log.d(TAG, "sensorListener not registered.");
            }
        }

        return data;
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
		
		HashMap sensorParam = getSensorData();

		HashMap commandParam = (HashMap) command.getParameters();
		Log.d(TAG, "Executing Command on Equipment: " + commandParam.get("equipment"));

        sendNotification(new EquipmentNotification(equipmentCode, sensorParam));

		return new CommandResult(CommandResult.STATUS_COMLETED,
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
