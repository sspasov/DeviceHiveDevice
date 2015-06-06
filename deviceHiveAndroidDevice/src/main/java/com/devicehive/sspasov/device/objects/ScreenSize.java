package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Equipment;
import com.dataart.android.devicehive.device.EquipmentNotification;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.utils.L;

import java.util.HashMap;

/**
 * Created by toni on 28.05.15.
 */
public class ScreenSize extends Equipment {

    private static final String TAG = ScreenSize.class.getSimpleName();
    private static final String NAME = "Screen Size";
    private static String CODE;
    private static final String TYPE = "screen_size";

    private Context mContext;

    public ScreenSize(Context context) {
        super(equipmentData(
                NAME,
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_ss",
                TYPE));
        this.mContext = context;

        CODE = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "_ss";
    }

    private static EquipmentData equipmentData(String name, String code, String type) {
        L.d(TAG, "equipmentData()");
        EquipmentData equipmentData = new EquipmentData(name, code, type);
        //ed.setData((Serializable) new TestEquipmentData(4236));
        return equipmentData;
    }


    public String getValue() {
        return getScreenSize();
    }

    private String getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        return String.valueOf(screenInches);
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