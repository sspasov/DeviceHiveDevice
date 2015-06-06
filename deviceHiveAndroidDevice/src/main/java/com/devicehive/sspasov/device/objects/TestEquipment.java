package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.provider.Settings;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Equipment;
import com.dataart.android.devicehive.device.EquipmentNotification;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.utils.L;

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
                        EquipmentTypeConverter.toString(sensor.getType()))
        );
        L.d(TAG, "Attaching equipment: " + sensor.getName());
        this.mContext = context;
        mSensor = sensor;

        equipmentName = mSensor.getName();
        equipmentCode = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_" + mSensor.getType();
        equipmentType = mSensor.getType();

        multipleValueEquipment = isMultipleValueEquipment();

        createSensorListener();
    }

    private boolean isMultipleValueEquipment() {
        L.d(TAG, "isMultipleValueEquipment()");
        return equipmentType != 2 && equipmentType != 14 && equipmentType != 5
                && equipmentType != 6 && equipmentType != 8 && equipmentType != 18
                && equipmentType != 19;
    }

    private void createSensorListener() {
        L.d(TAG, "createSensorListener()");
        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                x = event.values[0];
                //L.d(TAG, mSensor.getName()+" value x: "+x);

                if (multipleValueEquipment) {
                    //L.d(TAG, equipmentName);
                    y = event.values[1];
                    //L.d(TAG, mSensor.getName()+" value y: "+y);

                    z = event.values[2];
                    //L.d(TAG, mSensor.getName() + " value z: " + z);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    /**
     * @name - simple equipment name
     * @code - unique ID for the equipment. It's used to point and use this equipment.
     * @type - equipment type *
     */
    private static EquipmentData equipmentData(String name, String code, String type) {
        L.d(TAG, "equipmentData()");
        EquipmentData equipmentData = new EquipmentData(name, code, type);
        //ed.setData((Serializable) new TestEquipmentData(4236));
        return equipmentData;
    }

    public SensorEventListener getSensorListener() {
        L.d(TAG, "getSensorListener()");
        return sensorListener;
    }

    public Sensor getSensor() {
        L.d(TAG, "getSensor()");
        return mSensor;
    }

    public String getEquipmentName() {
        L.d(TAG, "getEquipmentName()");
        return equipmentName;
    }

    @Override
    public void onBeforeRunCommand(Command command) {

    }

    @Override
    public boolean shouldRunCommandAsynchronously(final Command command) {
        return DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION;
    }

    @Override
    public CommandResult runCommand(final Command command) {

        CommandInfo commandInfo = new CommandInfo(command);

        return execute(commandInfo);
    }

    private CommandResult execute(CommandInfo commandInfo) {
        L.d(TAG, "runCommand(" + commandInfo.getName() + ")");

        HashMap<String, Object> sensorParam = new HashMap<>();
        sensorParam.put("equipment", commandInfo.getInputParams().get("equipment"));

        Parameter p;

        if (multipleValueEquipment) {
            p = new Parameter("x", x + "");
            sensorParam.put(p.name, p.value);
            L.d(TAG, mSensor.getName() + " value x: " + x);

            p = new Parameter("y", y + "");
            sensorParam.put(p.name, p.value);
            L.d(TAG, mSensor.getName() + " value y: " + y);

            p = new Parameter("z", z + "");
            sensorParam.put(p.name, p.value);
            L.d(TAG, mSensor.getName() + " value z: " + z);
        } else {
            p = new Parameter("value", x + "");
            sensorParam.put(p.name, p.value);
            L.d(TAG, mSensor.getName() + " value: " + x);
        }

        commandInfo.setResult("Executed on " + equipmentName + "!");
        commandInfo.setStatus(CommandResult.STATUS_COMLETED);
        commandInfo.setOutputParams(sensorParam);

        L.d(TAG, "Executing Command on Equipment (" + commandInfo.getInputParams().get("equipment") + ")");

        sendNotification(new EquipmentNotification("Executed command \"" + commandInfo.getName() + "\"", commandInfo.getOutputParams()));

        return new CommandResult(commandInfo.getStatus(), commandInfo.getResult());
    }


    @Override
    protected boolean onRegisterEquipment() {
        L.d(TAG, "onRegisterEquipment()");
        return true;
    }

    @Override
    protected boolean onUnregisterEquipment() {
        L.d(TAG, "onUnregisterEquipment()");
        return true;
    }

    @Override
    protected void onStartProcessingCommands() {
        L.d(TAG, "onStartProcessingCommands()");
    }

    @Override
    protected void onStopProcessingCommands() {
        L.d(TAG, "onStopProcessingCommands()");
    }
}
