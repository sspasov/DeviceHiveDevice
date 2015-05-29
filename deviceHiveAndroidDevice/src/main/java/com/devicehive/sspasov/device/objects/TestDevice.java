package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.DeviceClass;
import com.dataart.android.devicehive.DeviceData;
import com.dataart.android.devicehive.Network;
import com.dataart.android.devicehive.Notification;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Device;
import com.devicehive.sspasov.device.BuildConfig;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.commands.DeviceCommand;
import com.devicehive.sspasov.device.utils.DeviceConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TestDevice extends Device {

	private static final String TAG = TestDevice.class.getSimpleName();

    private static final String DEVICE_PHONE = "Android Phone";
    private static final String DEVICE_TABLET = "Android Tablet";

    private Context mContext;
    private static String deviceID;
    private static String deviceName;

	private List<RegistrationListener> registrationListeners = new LinkedList<RegistrationListener>();
	private List<CommandListener> commandListeners = new LinkedList<CommandListener>();
	private List<NotificationListener> notificationListeners = new LinkedList<NotificationListener>();

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

	public TestDevice(Context context) {
		super(context, getTestDeviceData(context));
        this.mContext = context;

        /** Searching for equipment and adds it **/
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for(int i=0; i<deviceSensors.size(); i++) {
            if(deviceSensors.get(i).getType() <= 21) {
                TestEquipment testEquipment = new TestEquipment(context, deviceSensors.get(i));
                if(sensorManager.registerListener(
                        testEquipment.getSensorListener(),
                        testEquipment.getSensor(),
                        SensorManager.SENSOR_DELAY_NORMAL) ) {
                    Log.d(TAG, "successful registered sensor listener");
                    attachEquipment(testEquipment);
                } else {
                    Log.e(TAG, "sensor listener not registered");
                }
            } else {
                Log.w(TAG, "sensor not usable");
            }
            //Log.d(TAG, "Attaching equipment: "+testEquipment.getEquipmentName());
        }

       /* Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        TestEquipment te = new TestEquipment(context, sensor);
        sensorManager.registerListener(te.getSensorListener(), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        attachEquipment(te);
        Log.d(TAG, "Attaching equipment: "+sensor.getName());

        Sensor sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        TestEquipment te2 = new TestEquipment(context, sensor2);
        sensorManager.registerListener(te2.getSensorListener(), sensor2, SensorManager.SENSOR_DELAY_NORMAL);
        attachEquipment(te2);
        Log.d(TAG, "Attaching equipment: "+sensor2.getName());*/
	}


	private static DeviceData getTestDeviceData(Context context) {
		
		final Network network = new Network( //TODO: need to rethink this
				"SAMPLE_Test Android Network(Device Framework)",
				"SAMPLE_DESCRIPTION Test Android Device Network(Device Framework)");

		final DeviceClass deviceClass = new DeviceClass(
				getDeviceClass(context),
				getDeviceClassVersion(context),
				DeviceConfig.DEVICE_IS_PERMANENT,
                null); //DeviceConfig.DEVICE_TIMEOUT

        deviceID = getDeviceUniqueID(context);
        deviceName = android.os.Build.MODEL;

		final DeviceData deviceData = new DeviceData(
                deviceID, 					                    //DEVICE UNIQUE ID
				context.getString(R.string.key_device_registration),	//DEVICE REGISTER KEY //TODO: get key programmatically ?
                deviceName, 						            //DEVICE NAME
				DeviceData.DEVICE_STATUS_OK,					//DEVICE STATUS
				network,
				deviceClass);									//DEVICE CLASS TYPE

		return deviceData;
	}

	public static String getDeviceUniqueID(Context context){
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	private static String getDeviceClass(Context context) {
		if(isDeviceTablet(context)) {
			return DEVICE_TABLET;
		}
		return DEVICE_PHONE;
 	}

	private static boolean isDeviceTablet(Context activityContext) {
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
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return info != null ? info.versionName : BuildConfig.VERSION_NAME;
	}
	
	@Override
	public void onBeforeRunCommand(Command command) {
		Log.d(TAG, "onBeforeRunCommand: " + command.getCommand());
		notifyListenersCommandReceived(command);
	}

	@Override
	public CommandResult runCommand(final Command command) {

        CommandInfo commandInfo = new CommandInfo(command);

        return execute(commandInfo);
	}

    private CommandResult execute(CommandInfo commandInfo) {
        if(!commandInfo.getInputParams().containsKey("device")) {
            commandInfo.setStatus(CommandResult.STATUS_FAILED);
            commandInfo.setResult("Failed to execute command " + commandInfo.getName() + " on (" + deviceID + ")" + deviceName);
            return new CommandResult(commandInfo.getStatus(), commandInfo.getResult());
        } else {
            commandInfo.setType(commandInfo.getInputParams().get("device").toString());
        }

        Log.d(TAG, "Executing command \"" + commandInfo.getName() + "\" on " + TAG);

        //TODO: parameters in the command should set witch case to execute
        switch (commandInfo.getType()) {
            case DeviceCommand.GET_BATTERY_LEVEL: {
                commandInfo.setOutputParams(getBatteryLevel());
                commandInfo.setResult("Battery is: " + commandInfo.getOutputParams().get("value"));
				Log.d(TAG, "Successfully executed command \"" + commandInfo.getName() + "\" on " + TAG);
                break;
            }

            case DeviceCommand.GET_GPS_COORDINATES: {
                commandInfo.setOutputParams(getGPSCoordinates());
                commandInfo.setResult("GPS coordinates are: " + commandInfo.getOutputParams().get("value"));
                Log.d(TAG, "Successfully executed command \""  + commandInfo.getName() + "\" on " + TAG);
                break;
            }

            case DeviceCommand.GET_TIME_ON: {
                commandInfo.setOutputParams(getTimeOn());
                commandInfo.setResult("System Time On is: " + commandInfo.getOutputParams().get("value"));
                Log.d(TAG, "Successfully executed command \"" + commandInfo.getName() + "\" on " + TAG);
                break;
            }

            case DeviceCommand.GET_SCREEN_SIZE: {
                commandInfo.setOutputParams(getScreenSize());
                commandInfo.setResult("Screen size is: " + commandInfo.getOutputParams().get("value"));
                Log.d(TAG, "Successfully executed command \"" + commandInfo.getName() + "\" on " + TAG);
                break;
            }

            default: {
                commandInfo.setStatus(CommandResult.STATUS_FAILED);
                commandInfo.setResult("Failed to execute command " + commandInfo.getName() + " on (" + deviceID + ")" + deviceName);
				Log.d(TAG, "Failed to execute command \""  + commandInfo.getName() + "\" on "+TAG);
            }
        }

        sendNotification(new Notification("Executed command \"" + commandInfo.getName() + "\"", commandInfo.getOutputParams()));
        return new CommandResult(commandInfo.getStatus(), commandInfo.getResult());
    }

    private HashMap getGPSCoordinates() {
        //TODO: implementation
        //
        HashMap param = new HashMap();
        //
        return param;
    }

    private HashMap getTimeOn() {
        //TODO: implementation
        //
        HashMap param = new HashMap();
        //
        return param;
    }

    private HashMap getBatteryLevel() {
        Battery battery = Battery.getInstance(mContext);
        HashMap param = new HashMap();
		param.put("device", DeviceCommand.GET_BATTERY_LEVEL);
        param.put(battery.getName(), battery.getValue());
        return param;
    }

    private HashMap getScreenSize() {
        ScreenSize screenSize = ScreenSize.getInstance(mContext);
        HashMap param = new HashMap();
        param.put("device", DeviceCommand.GET_SCREEN_SIZE);
        param.put(screenSize.getName(), screenSize.getValue());
        return param;
    }

	@Override
	public boolean shouldRunCommandAsynchronously(final Command command) {
		return DeviceConfig.DEVICE_ASYNC_COMMAND_EXECUTION;
	}

	public void addDeviceListener(RegistrationListener listener) {
		registrationListeners.add(listener);
	}

	public void removeDeviceListener(RegistrationListener listener) {
		registrationListeners.remove(listener);
	}

	public void addCommandListener(CommandListener listener) {
		commandListeners.add(listener);
	}

	public void removeCommandListener(CommandListener listener) {
		commandListeners.remove(listener);
	}

	public void addNotificationListener(NotificationListener listener) {
		notificationListeners.add(listener);
	}

	public void removeNotificationListener(NotificationListener listener) {
		notificationListeners.remove(listener);
	}

	public void removeListener(Object listener) {
		registrationListeners.remove(listener);
		commandListeners.remove(listener);
		notificationListeners.remove(listener);
	}

	@Override
	protected void onStartRegistration() {
		Log.d(TAG, "onStartRegistration");
	}

	@Override
	protected void onFinishRegistration() {
		Log.d(TAG, "onFinishRegistration");
		notifyListenersDeviceRegistered();
	}

	@Override
	protected void onFailRegistration() {
		Log.d(TAG, "onFailRegistration");
		notifyListenersDeviceFailedToRegister();
	}

	@Override
	protected void onStartProcessingCommands() {
		Log.d(TAG, "onStartProcessingCommands");
	}

	@Override
	protected void onStopProcessingCommands() {
		Log.d(TAG, "onStopProcessingCommands");
	}

	@Override
	protected void onStartSendingNotification(Notification notification) {
		Log.d(TAG, "onStartSendingNotification : " + notification.getName());
		notifyListenersDeviceStartSendingNotification(notification);
	}

	@Override
	protected void onFinishSendingNotification(Notification notification) {
		Log.d(TAG, "onFinishSendingNotification: " + notification.getId());
		notifyListenersDeviceSentNotification(notification);
	}

	@Override
	protected void onFailSendingNotification(Notification notification) {
		Log.d(TAG, "onFailSendingNotification: " + notification.getName());
		notifyListenersDeviceFailedToSendNotification(notification);
	}

	private void notifyListenersCommandReceived(Command command) {
        Log.d(TAG, "notifyListenersCommandReceived: " + command.toString());
		for (CommandListener listener : commandListeners) {
			listener.onDeviceReceivedCommand(command);
		}
	}

	private void notifyListenersDeviceRegistered() {
        Log.d(TAG, "notifyListenersDeviceRegistered");
		for (RegistrationListener listener : registrationListeners) {
			listener.onDeviceRegistered();
		}
	}

	private void notifyListenersDeviceFailedToRegister() {
        Log.d(TAG, "notifyListenersDeviceFailedToRegister");
		for (RegistrationListener listener : registrationListeners) {
			listener.onDeviceFailedToRegister();
		}
	}

	private void notifyListenersDeviceStartSendingNotification(Notification notification) {
		Log.d(TAG, "notifyListenersDeviceStartSendingNotification: "+notification.getName());
		for (NotificationListener listener : notificationListeners) {
			listener.onDeviceStartSendingNotification(notification);
		}
	}

	private void notifyListenersDeviceSentNotification(Notification notification) {
		Log.d(TAG, "notifyListenersDeviceSentNotification: "+notification.getName());
		for (NotificationListener listener : notificationListeners) {
			listener.onDeviceSentNotification(notification);
		}
	}

	private void notifyListenersDeviceFailedToSendNotification(Notification notification) {
        Log.d(TAG, "notifyListenersDeviceFailedToSendNotification: "+notification.getName());
		for (NotificationListener listener : notificationListeners) {
			listener.onDeviceFailedToSendNotification(notification);
		}
	}

}
