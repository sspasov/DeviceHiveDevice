package com.devicehive.sspasov.device.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
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

import java.util.LinkedList;
import java.util.List;

public class DeviceActivity extends SherlockFragmentActivity implements
        RegistrationListener, ParameterProvider, CommandListener,
        NotificationListener, NotificationSender, ParameterDialogListener {

    private static final String TAG = DeviceActivity.class.getSimpleName();

    private TestDevice device;

    private DeviceInformationFragment deviceInfoFragment;
    private EquipmentListFragment equipmentListFragment;
    private DeviceCommandsFragment deviceCommandsFragment;
    private DeviceSendNotificationFragment deviceSendNotificationFragment;

    private List<Command> receivedCommands = new LinkedList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_device);

        if (DeviceConfig.FIRST_STARTUP) {
            Intent startupActivity = new Intent(this, StartupConfigurationActivity.class);
            startActivity(startupActivity);
            finish();
        }

        device = new TestDevice(getApplicationContext());
        device.setDebugLoggingEnabled(BuildConfig.DEBUG);
        device.setApiEnpointUrl(DeviceConfig.API_ENDPOINT);


        ActionBar ab = getSupportActionBar();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        device.removeDeviceListener(this);
        device.removeCommandListener(this);
        device.removeNotificationListener(this);
        device.stopProcessingCommands();

        Log.e(TAG, "device.unregisterDevice()");
        device.unregisterDevice();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        /*device.removeDeviceListener(this);
        device.removeCommandListener(this);
        device.removeNotificationListener(this);
        device.stopProcessingCommands();
        if (isFinishing()) {
            Log.e(TAG, "device.unregisterDevice()");
            device.unregisterDevice();
        }*/
    }

    @Override
    public void onDeviceRegistered() {
        deviceInfoFragment.setDeviceData(device.getDeviceData());
        device.startProcessingCommands();
    }

    @Override
    public void onDeviceFailedToRegister() {
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
        receivedCommands.add(command);
        deviceCommandsFragment.setCommands(receivedCommands);
    }

    @Override
    public void onDeviceStartSendingNotification(Notification notification) {
        Log.d(TAG, "Start sending notification: " + notification.getName());
        if (notification.getName().contains("DeviceStatus")) {
            showDialog("Success!", notification.getName() + " has been sent.");
        } else {
            showDialog("Success!", notification.getName());
        }

    }

    @Override
    public void onDeviceSentNotification(Notification notification) {
        Log.d(TAG, "Finish sending notification: " + notification.getId());
        //showDialog("Success!", "Notification sent with ID " + notification.getId());
        Toast.makeText(this, "Notification registered with ID " + notification.getId(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceFailedToSendNotification(Notification notification) {
        Log.d(TAG, "Fail sending notification: " + notification.getName());
        showErrorDialog("Failed to send notification: " + notification.getName());
    }

    @Override
    public void sendNotification(Notification notification) {
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

    private com.actionbarsherlock.view.Menu optionsMenu;

    private static final int MENU_ID_SETTINGS = 0x01;

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        optionsMenu = menu;
        menu.add(com.actionbarsherlock.view.Menu.NONE, MENU_ID_SETTINGS,
                com.actionbarsherlock.view.Menu.NONE, "Settings")
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
                Log.d(TAG, "Changed settings!");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(
            com.actionbarsherlock.view.MenuItem item) {
        if (item.getItemId() == MENU_ID_SETTINGS) {
            startActivityForResult(new Intent(this, SettingsActivity.class),
                    SETTINGS_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
