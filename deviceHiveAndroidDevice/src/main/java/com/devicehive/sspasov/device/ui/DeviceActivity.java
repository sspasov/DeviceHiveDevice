package com.devicehive.sspasov.device.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.Notification;
import com.devicehive.sspasov.device.BuildConfig;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.adapters.TabsAdapter;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.dialogs.ParameterDialog;
import com.devicehive.sspasov.device.dialogs.ParameterDialog.ParameterDialogListener;
import com.devicehive.sspasov.device.fragments.DeviceCommandsFragment;
import com.devicehive.sspasov.device.fragments.DeviceInformationFragment;
import com.devicehive.sspasov.device.fragments.DeviceSendNotificationFragment;
import com.devicehive.sspasov.device.fragments.DeviceSendNotificationFragment.NotificationSender;
import com.devicehive.sspasov.device.fragments.DeviceSendNotificationFragment.ParameterProvider;
import com.devicehive.sspasov.device.fragments.EquipmentListFragment;
import com.devicehive.sspasov.device.objects.TestDevice;
import com.devicehive.sspasov.device.objects.TestDevice.CommandListener;
import com.devicehive.sspasov.device.objects.TestDevice.NotificationListener;
import com.devicehive.sspasov.device.objects.TestDevice.RegistrationListener;
import com.devicehive.sspasov.device.utils.L;

import java.util.LinkedList;
import java.util.List;

public class DeviceActivity extends FragmentActivity implements
        RegistrationListener, ParameterProvider, CommandListener,
        NotificationListener, NotificationSender, ParameterDialogListener {

    private static final String TAG = DeviceActivity.class.getSimpleName();


    public static boolean registerDevice = true;
    private NetworkReceiver receiver = new NetworkReceiver();

    private TestDevice device;

    private DeviceInformationFragment deviceInfoFragment;
    private EquipmentListFragment equipmentListFragment;
    private DeviceCommandsFragment deviceCommandsFragment;
    private DeviceSendNotificationFragment deviceSendNotificationFragment;

    private List<Command> receivedCommands = new LinkedList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        setContentView(R.layout.activity_device);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);

        if (DeviceConfig.FIRST_STARTUP && (DeviceConfig.API_ENDPOINT == null)) {
            Intent startupActivity = new Intent(this, StartupConfigurationActivity.class);
            startActivity(startupActivity);
            finish();
        } else {
            device = new TestDevice(getApplicationContext());
            device.setDebugLoggingEnabled(BuildConfig.DEBUG);
            device.setApiEnpointUrl(DeviceConfig.API_ENDPOINT);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar ab = getActionBar();
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ab.setTitle(getString(R.string.test_device));

            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

            TabsAdapter tabsAdapter = new TabsAdapter(this, viewPager);

            deviceInfoFragment = new DeviceInformationFragment();
            deviceInfoFragment.setDeviceData(device.getDeviceData());

            equipmentListFragment = new EquipmentListFragment();
            equipmentListFragment.setEquipment(device.getDeviceData().getEquipment());

            deviceCommandsFragment = new DeviceCommandsFragment();

            deviceSendNotificationFragment = new DeviceSendNotificationFragment();
            deviceSendNotificationFragment.setParameterProvider(this);
            deviceSendNotificationFragment.setEquipment(device.getDeviceData().getEquipment());

            tabsAdapter.addTab(ab.newTab().setText(getString(R.string.tab_device_info)), deviceInfoFragment);
            tabsAdapter.addTab(ab.newTab().setText(getString(R.string.tab_device_equipment)), equipmentListFragment);
            tabsAdapter.addTab(ab.newTab().setText(getString(R.string.tab_device_commands)), deviceCommandsFragment);
            tabsAdapter.addTab(ab.newTab().setText(getString(R.string.tab_device_send_notification)), deviceSendNotificationFragment);
        }

    }

    private void createNetErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
                .setTitle("Unable to connect")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d(TAG, "onResume()");
        if (registerDevice && device != null && DeviceConfig.API_ENDPOINT != null) {
            deviceUnregister();
            deviceRegister();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d(TAG, "onStop()");
        deviceUnregister();
    }

    public void deviceRegister() {
        L.d(TAG, "deviceRegister()");
        device.addDeviceListener(this);
        device.addCommandListener(this);
        device.addNotificationListener(this);
        deviceInfoFragment.setDeviceData(device.getDeviceData());
        if (!device.isRegistered()) {
            device.registerDevice();
        } else {
            device.startProcessingCommands();
        }
    }

    public void deviceUnregister() {
        L.d(TAG, "deviceUnregister()");
        device.removeDeviceListener(this);
        device.removeCommandListener(this);
        device.removeNotificationListener(this);
        device.stopProcessingCommands();

        device.unregisterDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d(TAG, "onDestroy()");
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    public void onDeviceRegistered() {
        L.d(TAG, "onDeviceRegistered()");
        deviceInfoFragment.setDeviceData(device.getDeviceData());
        device.startProcessingCommands();
    }

    @Override
    public void onDeviceFailedToRegister() {
        L.d(TAG, "onDeviceFailedToRegister()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder
                .setTitle("Error")
                .setMessage("Failed to register device")
                .setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                device.registerDevice();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    @Override
    public void onDeviceReceivedCommand(Command command) {
        L.d(TAG, "onDeviceReceivedCommand()");
        receivedCommands.add(command);
        deviceCommandsFragment.setCommands(receivedCommands);
    }

    @Override
    public void onDeviceStartSendingNotification(Notification notification) {
        L.d(TAG, "onDeviceStartSendingNotification(" + notification.getName() + ")");
        L.d(TAG, "onDeviceStartSendingNotification(" + notification.getId() + ")");
        if (notification.getName().contains("DeviceStatus")) {
            Toast.makeText(this, notification.getName() + " has been sent.", Toast.LENGTH_SHORT).show();
            //showDialog("Success!", notification.getName() + " has been sent.");
        } else {
            Toast.makeText(this, notification.getName(), Toast.LENGTH_SHORT).show();
            //showDialog("Success!", notification.getName());
        }
    }

    @Override
    public void onDeviceSentNotification(Notification notification) {
        L.d(TAG, "onDeviceSentNotification(" + notification.getId() + ")");
        //showDialog("Success!", "Notification sent with ID " + notification.getId());
        Toast.makeText(this, "Notification registered with ID " + notification.getId(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceFailedToSendNotification(Notification notification) {
        L.d(TAG, "onDeviceFailedToSendNotification(" + notification.getName() + ")");
        showErrorDialog("Failed to send notification: " + notification.getName());
    }

    @Override
    public void sendNotification(Notification notification) {
        L.d(TAG, "sendNotification()");
        device.sendNotification(notification);
    }

    protected void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setTitle(title).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    protected void showErrorDialog(String message) {
        showDialog("Error!", message);
    }

    @Override
    public void queryParameter() {
        FragmentManager fm = getSupportFragmentManager();
        final ParameterDialog parameterDialog = new ParameterDialog();
        parameterDialog.show(fm, ParameterDialog.TAG);
    }

    @Override
    public void onFinishEditingParameter(String name, String value) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
            deviceSendNotificationFragment.addParameter(name, value);
        }
    }


    private static final int MENU_ID_SETTINGS = 0x01;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_SETTINGS, Menu.NONE, "Settings")
                .setIcon(R.drawable.ic_menu_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    private static final int SETTINGS_REQUEST_CODE = 0x01;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                L.d(TAG, "Changed settings!");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_SETTINGS:
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                registerDevice = true;
                Toast.makeText(context, "WIFI connected", Toast.LENGTH_LONG).show();
                L.d(TAG, "WIFI connected");
                onResume();
            } else if (networkInfo != null) {
                Toast.makeText(context, "Mobile Data connected", Toast.LENGTH_LONG).show();
                L.d(TAG, "Mobile Data connected");
                registerDevice = true;
                onResume();
            } else {
                registerDevice = false;
                Toast.makeText(context, "Connection lost", Toast.LENGTH_LONG).show();
                L.d(TAG, "Connection lost");
                deviceUnregister();
            }
        }
    }

}
