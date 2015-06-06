package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Equipment;
import com.dataart.android.devicehive.device.EquipmentNotification;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.utils.L;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by toni on 29.05.15.
 */
public class DeviceTimeOn extends Equipment {

    private static final String TAG = DeviceTimeOn.class.getSimpleName();
    private static final String NAME = "Time On";
    private static String CODE;
    private static final String TYPE = "time_on";

    private Context mContext;

    public DeviceTimeOn(Context context) {
        super(equipmentData(
                NAME,
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_to",
                TYPE));
        this.mContext = context;

        CODE = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_to";
    }

    private static EquipmentData equipmentData(String name, String code, String type) {
        L.d(TAG, "equipmentData()");
        EquipmentData equipmentData = new EquipmentData(name, code, type);
        //ed.setData((Serializable) new TestEquipmentData(4236));
        return equipmentData;
    }

    public String getValue() {
        return getTimeOn();
    }

    private String getTimeOn() {
        long millis = SystemClock.elapsedRealtime();
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return String.valueOf(hms);
    }

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
}

