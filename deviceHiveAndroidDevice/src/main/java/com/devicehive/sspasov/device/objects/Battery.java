package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.provider.Settings;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Equipment;
import com.dataart.android.devicehive.device.EquipmentNotification;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.utils.L;

import java.util.HashMap;

/**
 * Created by toni on 23.05.15.
 */
public class Battery extends Equipment {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = Battery.class.getSimpleName();
    private static final String NAME = "Battery";
    private static final String TYPE = "battery";

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private Context mContext;
    private static String CODE;

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public Battery(Context context) {
        super(equipmentData(
                NAME,
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_b",
                TYPE));
        this.mContext = context;

        CODE = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_b";
    }

    public String getValue() {
        return batteryLevel(mContext);
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------
    private static EquipmentData equipmentData(String name, String code, String type) {
        L.d(TAG, "equipmentData()");
        EquipmentData equipmentData = new EquipmentData(name, code, type);
        //ed.setData((Serializable) new TestEquipmentData(4236));
        return equipmentData;
    }

    private String batteryLevel(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = intent != null ? intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) : 0;
        int scale = intent != null ? intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100) : 0;
        int percent = (level * 100) / scale;
        return String.valueOf(percent) + "%";
    }

    private CommandResult execute(CommandInfo commandInfo) {
        L.d(TAG, "runCommand(" + commandInfo.getName() + ")");

        HashMap<String, Object> sensorParam = new HashMap<>();
        sensorParam.put("equipment", commandInfo.getInputParams().get("equipment"));

        String x = getValue();

        Parameter p = new Parameter("value", x + "");
        sensorParam.put(p.name, p.value);
        L.d(TAG, NAME + " value: " + x);

        commandInfo.setResult("Executed on " + NAME + "!");
        commandInfo.setStatus(CommandResult.STATUS_COMLETED);
        commandInfo.setOutputParams(sensorParam);

        L.d(TAG, "Executing Command on Equipment (" + commandInfo.getInputParams().get("equipment") + ")");

        sendNotification(new EquipmentNotification("Executed command \"" + commandInfo.getName() + "\"", commandInfo.getOutputParams()));

        return new CommandResult(commandInfo.getStatus(), commandInfo.getResult());
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onBeforeRunCommand(Command command) {
        L.d(TAG, "onBeforeRunCommand(" + command.getCommand() + ")");
    }

    @Override
    public boolean shouldRunCommandAsynchronously(Command command) {
        return DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION;
    }

    @Override
    public CommandResult runCommand(Command command) {
        CommandInfo commandInfo = new CommandInfo(command);
        return execute(commandInfo);
    }
}
