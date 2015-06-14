package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.BuildConfig;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.DeviceClass;
import com.dataart.android.devicehive.DeviceData;
import com.dataart.android.devicehive.Network;
import com.dataart.android.devicehive.Notification;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Device;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.utils.L;

import java.util.LinkedList;
import java.util.List;

public class TestDevice extends Device {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = TestDevice.class.getSimpleName();

    private static final String DEVICE_PHONE = "Android Phone";
    private static final String DEVICE_TABLET = "Android Tablet";

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private Context mContext;
    private static String deviceID;
    private static String deviceName;

    private List<RegistrationListener> registrationListeners = new LinkedList<>();
    private List<CommandListener> commandListeners = new LinkedList<>();
    private List<NotificationListener> notificationListeners = new LinkedList<>();

    // ---------------------------------------------------------------------------------------------
    // Interfaces
    // ---------------------------------------------------------------------------------------------
    public interface RegistrationListener {
        void onDeviceRegistered();

        void onDeviceFailedToRegister();
    }

    public interface CommandListener {
        void onDeviceReceivedCommand(Command command);
    }

    public interface NotificationListener {
        void onDeviceStartSendingNotification(Notification notification);

        void onDeviceSentNotification(Notification notification);

        void onDeviceFailedToSendNotification(Notification notification);
    }

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public TestDevice(Context context) {
        super(context, getTestDeviceData(context));
        L.d(TAG, "TestDevice()");
        this.mContext = context;
        addEquipment();
    }

    public void addDeviceListener(RegistrationListener listener) {
        L.d(TAG, "addDeviceListener()");
        registrationListeners.add(listener);
    }

    public void removeDeviceListener(RegistrationListener listener) {
        L.d(TAG, "removeDeviceListener()");
        registrationListeners.remove(listener);
    }

    public void addCommandListener(CommandListener listener) {
        L.d(TAG, "addCommandListener()");
        commandListeners.add(listener);
    }

    public void removeCommandListener(CommandListener listener) {
        L.d(TAG, "removeCommandListener()");
        commandListeners.remove(listener);
    }

    public void addNotificationListener(NotificationListener listener) {
        L.d(TAG, "addNotificationListener()");
        notificationListeners.add(listener);
    }

    public void removeNotificationListener(NotificationListener listener) {
        L.d(TAG, "removeNotificationListener()");
        notificationListeners.remove(listener);
    }

    public void removeListener(Object listener) {
        L.d(TAG, "removeListener()");
        registrationListeners.remove(listener);
        commandListeners.remove(listener);
        notificationListeners.remove(listener);
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------

    /**
     * Searching for equipment and adds it *
     */
    private void addEquipment() {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < deviceSensors.size(); i++) {
            if (deviceSensors.get(i).getType() <= 21) {
                TestEquipment testEquipment = new TestEquipment(mContext, deviceSensors.get(i));
                if (sensorManager.registerListener(
                        testEquipment.getSensorListener(),
                        testEquipment.getSensor(),
                        SensorManager.SENSOR_DELAY_NORMAL)) {
                    L.d(TAG, "Successful registered sensor listener");
                    attachEquipment(testEquipment);
                } else {
                    L.e(TAG, "Failed to register sensor listener");
                }
            } else {
                L.w(TAG, "Sensor is not usable");
            }
        }

        Battery battery = new Battery(mContext);
        attachEquipment(battery);

        DeviceTimeOn deviceTimeOn = new DeviceTimeOn(mContext);
        attachEquipment(deviceTimeOn);

        ScreenSize screenSize = new ScreenSize(mContext);
        attachEquipment(screenSize);
    }


    private static DeviceData getTestDeviceData(Context context) {
        L.d(TAG, "getTestDeviceData()");

        Network network = new Network(
                DeviceConfig.NETWORK_NAME,
                DeviceConfig.NETWORK_DESCRIPTION);

        DeviceClass deviceClass = new DeviceClass(
                getDeviceClass(context),                           //device class
                getDeviceClassVersion(context),                    //device class version
                DeviceConfig.DEVICE_IS_PERMANENT,                  //permanent class
                DeviceConfig.DEVICE_TIMEOUT);                      //device goes offline timeoutT

        deviceID = getDeviceUniqueID(context);
        deviceName = Build.MANUFACTURER + " " + Build.MODEL;

        DeviceData deviceData = new DeviceData(
                deviceID,                                          //DEVICE UNIQUE ID
                context.getString(R.string.key_device_registration),    //DEVICE REGISTER KEY
                deviceName,                                        //DEVICE NAME
                DeviceData.DEVICE_STATUS_OK,                       //DEVICE STATUS
                network,                                           //DEVICE NETWORK
                deviceClass);                                      //DEVICE CLASS TYPE

        return deviceData;
    }

    private static String getDeviceUniqueID(Context context) {
        L.d(TAG, "getDeviceUniqueID()");
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getDeviceClass(Context context) {
        L.d(TAG, "getDeviceClass()");
        if (isDeviceTablet(context)) {
            return DEVICE_TABLET + " " + deviceID;
        }
        return DEVICE_PHONE + " " + deviceID;
    }

    private static boolean isDeviceTablet(Context activityContext) {
        L.d(TAG, "isDeviceTablet()");
        boolean device_large = ((activityContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        if (device_large) {
            DisplayMetrics metrics = new DisplayMetrics();
            //activity = (Activity) activityContext.getApplicationContext();;
            WindowManager wm = (WindowManager) activityContext.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        }
        return false;
    }

    private static String getDeviceClassVersion(Context context) {
        L.d(TAG, "getDeviceClassVersion()");
        //TODO: android version must be used as class version. HOW ?
        //return Build.VERSION.RELEASE;
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info != null ? info.versionName : BuildConfig.VERSION_NAME;
    }

    private CommandResult execute(CommandInfo commandInfo) {
        L.d(TAG, "Executing command \"" + commandInfo.getName() + "\" on " + TAG);
        sendNotification(new Notification("Executed command \"" + commandInfo.getName() + "\"", commandInfo.getOutputParams()));
        return new CommandResult(commandInfo.getStatus(), commandInfo.getResult());
    }

    private void notifyListenersCommandReceived(Command command) {
        L.d(TAG, "notifyListenersCommandReceived(" + command.toString() + ")");
        for (CommandListener listener : commandListeners) {
            listener.onDeviceReceivedCommand(command);
        }
    }

    private void notifyListenersDeviceRegistered() {
        L.d(TAG, "notifyListenersDeviceRegistered()");
        for (RegistrationListener listener : registrationListeners) {
            listener.onDeviceRegistered();
        }
    }

    private void notifyListenersDeviceFailedToRegister() {
        L.d(TAG, "notifyListenersDeviceFailedToRegister()");
        for (RegistrationListener listener : registrationListeners) {
            listener.onDeviceFailedToRegister();
        }
    }

    private void notifyListenersDeviceStartSendingNotification(Notification notification) {
        L.d(TAG, "notifyListenersDeviceStartSendingNotification(" + notification.getName() + ")");
        for (NotificationListener listener : notificationListeners) {
            listener.onDeviceStartSendingNotification(notification);
        }
    }

    private void notifyListenersDeviceSentNotification(Notification notification) {
        L.d(TAG, "notifyListenersDeviceSentNotification(" + notification.getName() + ")");
        for (NotificationListener listener : notificationListeners) {
            listener.onDeviceSentNotification(notification);
        }
    }

    private void notifyListenersDeviceFailedToSendNotification(Notification notification) {
        L.d(TAG, "notifyListenersDeviceFailedToSendNotification(" + notification.getName() + ")");
        for (NotificationListener listener : notificationListeners) {
            listener.onDeviceFailedToSendNotification(notification);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onBeforeRunCommand(Command command) {
        L.d(TAG, "onBeforeRunCommand(" + command.getCommand() + ")");
        notifyListenersCommandReceived(command);
    }

    @Override
    public CommandResult runCommand(final Command command) {
        L.d(TAG, "runCommand()");
        CommandInfo commandInfo = new CommandInfo(command);
        return execute(commandInfo);
    }

    @Override
    public boolean shouldRunCommandAsynchronously(final Command command) {
        L.d(TAG, "shouldRunCommandAsynchronously()");
        return DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION;
    }

    @Override
    protected void onStartRegistration() {
        L.d(TAG, "onStartRegistration()");
    }

    @Override
    protected void onFinishRegistration() {
        L.d(TAG, "onFinishRegistration()");
        notifyListenersDeviceRegistered();
    }

    @Override
    protected void onFailRegistration() {
        L.d(TAG, "onFailRegistration()");
        notifyListenersDeviceFailedToRegister();
    }

    @Override
    protected void onStartProcessingCommands() {
        L.d(TAG, "onStartProcessingCommands()");
    }

    @Override
    protected void onStopProcessingCommands() {
        L.d(TAG, "onStopProcessingCommands()");
    }

    @Override
    protected void onStartSendingNotification(Notification notification) {
        L.d(TAG, "onStartSendingNotification(" + notification.getName() + ")");
        notifyListenersDeviceStartSendingNotification(notification);
    }

    @Override
    protected void onFinishSendingNotification(Notification notification) {
        L.d(TAG, "onFinishSendingNotification(" + notification.getId() + ")");
        notifyListenersDeviceSentNotification(notification);
    }

    @Override
    protected void onFailSendingNotification(Notification notification) {
        L.d(TAG, "onFailSendingNotification(" + notification.getName() + ")");
        notifyListenersDeviceFailedToSendNotification(notification);
    }
}
