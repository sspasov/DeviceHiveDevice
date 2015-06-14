package com.devicehive.sspasov.device.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.Notification;
import com.devicehive.sspasov.device.BuildConfig;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.adapters.SimplePagerAdapter;
import com.devicehive.sspasov.device.config.DeviceConfig;
import com.devicehive.sspasov.device.objects.TestDevice;
import com.devicehive.sspasov.device.objects.TestDevice.CommandListener;
import com.devicehive.sspasov.device.objects.TestDevice.NotificationListener;
import com.devicehive.sspasov.device.objects.TestDevice.RegistrationListener;
import com.devicehive.sspasov.device.receivers.NetworkReceiver;
import com.devicehive.sspasov.device.ui.dialogs.ParameterDialog;
import com.devicehive.sspasov.device.ui.dialogs.ParameterDialog.ParameterDialogListener;
import com.devicehive.sspasov.device.ui.fragments.DeviceCommandsFragment;
import com.devicehive.sspasov.device.ui.fragments.DeviceInformationFragment;
import com.devicehive.sspasov.device.ui.fragments.DeviceSendNotificationFragment;
import com.devicehive.sspasov.device.ui.fragments.DeviceSendNotificationFragment.NotificationSender;
import com.devicehive.sspasov.device.ui.fragments.DeviceSendNotificationFragment.ParameterProvider;
import com.devicehive.sspasov.device.ui.fragments.EquipmentListFragment;
import com.devicehive.sspasov.device.utils.L;
import com.devicehive.sspasov.device.views.SlidingTabLayout;

import java.util.LinkedList;
import java.util.List;

public class DeviceActivity extends AppCompatActivity implements
        RegistrationListener, ParameterProvider, CommandListener,
        NotificationListener, NotificationSender, ParameterDialogListener, NetworkReceiver.NetworkReceiverListener {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = DeviceActivity.class.getSimpleName();
    private static final int SETTINGS_REQUEST_CODE = 0x01;
    public static final String API_CHANGED = "apiChanged";

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    public TestDevice device;

    private DeviceInformationFragment deviceInformationFragment;
    private EquipmentListFragment equipmentListFragment;
    private DeviceCommandsFragment deviceCommandsFragment;
    private DeviceSendNotificationFragment deviceSendNotificationFragment;

    private List<Command> receivedCommands = new LinkedList<>();

    private TextView tvDeviceNotRegistered;

    // ---------------------------------------------------------------------------------------------
    // Activity life cycle
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        setContentView(R.layout.activity_device);

        tvDeviceNotRegistered = (TextView) findViewById(R.id.tv_device_not_registered);


        NetworkReceiver.setNetworkReceiverListener(this);

        if (DeviceConfig.FIRST_STARTUP && (DeviceConfig.API_ENDPOINT == null)) {
            Intent startupActivity = new Intent(this, StartupConfigurationActivity.class);
            startActivity(startupActivity);
            finish();
        } else {
            device = new TestDevice(getApplicationContext());
            device.setDebugLoggingEnabled(BuildConfig.DEBUG);
            device.setApiEnpointUrl(DeviceConfig.API_ENDPOINT);

            setupFragments();

            setupToolbar();

            ViewPager viewPager = setupViewPager();

            setupSlidingTabLayout(viewPager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d(TAG, "onResume()");
        if (NetworkReceiver.isConnected() && device != null && DeviceConfig.API_ENDPOINT != null) {
            deviceUnregister();
            deviceRegister();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d(TAG, "onDestroy()");
        deviceUnregister();
    }

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public void deviceRegister() {
        L.d(TAG, "deviceRegister()");
        device = null;
        device = new TestDevice(getApplicationContext());
        device.setDebugLoggingEnabled(BuildConfig.DEBUG);
        device.setApiEnpointUrl(DeviceConfig.API_ENDPOINT);
        device.addDeviceListener(this);
        device.addCommandListener(this);
        device.addNotificationListener(this);
        deviceInformationFragment.setDeviceData(device.getDeviceData());
        if (!device.isRegistered()) {
            device.registerDevice();
        } else {
            device.startProcessingCommands();
        }
    }

    public void deviceUnregister() {
        L.d(TAG, "deviceUnregister()");
        if (device != null) {
            device.removeDeviceListener(this);
            device.removeCommandListener(this);
            device.removeNotificationListener(this);
            device.stopProcessingCommands();

            device.unregisterDevice();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------
    private void setupFragments() {
        deviceInformationFragment = DeviceInformationFragment.newInstance();
        deviceInformationFragment.setDeviceData(device.getDeviceData());
        deviceInformationFragment.setContext(this);

        equipmentListFragment = EquipmentListFragment.newInstance();
        equipmentListFragment.setEquipment(device.getDeviceData().getEquipment());

        deviceCommandsFragment = DeviceCommandsFragment.newInstance();

        deviceSendNotificationFragment = DeviceSendNotificationFragment.newInstance();
        deviceSendNotificationFragment.setParameterProvider(this);
        deviceSendNotificationFragment.setEquipment(device.getDeviceData().getEquipment());
        deviceSendNotificationFragment.setDeviceStatus(device.getDeviceData().getStatus());
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private ViewPager setupViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SimplePagerAdapter(this, getSupportFragmentManager()));
        return viewPager;
    }

    private void setupSlidingTabLayout(ViewPager viewPager) {
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(viewPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getColorBasedForPosition(position);
            }
        });
    }

    private int getColorBasedForPosition(int pos) {
        int color;
        switch (pos) {
            case 0:
                color = getResources().getColor(R.color.info_strip);
                break;
            case 1:
                color = getResources().getColor(R.color.equipment_strip);
                break;
            case 2:
                color = getResources().getColor(R.color.commands_strip);
                break;
            default:
                color = getResources().getColor(R.color.notifications_strip);
                break;
        }

        return color;
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onDeviceRegistered() {
        L.d(TAG, "onDeviceRegistered()");
        tvDeviceNotRegistered.setVisibility(View.GONE);
        deviceInformationFragment.setDeviceData(device.getDeviceData());
        device.startProcessingCommands();
    }

    @Override
    public void onDeviceFailedToRegister() {
        L.d(TAG, "onDeviceFailedToRegister()");
        tvDeviceNotRegistered.setVisibility(View.VISIBLE);
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
        } else {
            Toast.makeText(this, notification.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeviceSentNotification(Notification notification) {
        L.d(TAG, "onDeviceSentNotification(" + notification.getId() + ")");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra(API_CHANGED)) {
                    Toast.makeText(this, "API Endpoint was changed. Please reselect network.", Toast.LENGTH_LONG).show();
                    Intent networkConfigurationActivity = new Intent(this, NetworkConfigurationActivity.class);
                    networkConfigurationActivity.putExtra("from", TAG);
                    startActivity(networkConfigurationActivity);
                    finish();
                }
                L.d(TAG, "Changed settings!");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnection() {
        onResume();
    }

    @Override
    public void onLostConnection() {
        deviceUnregister();
    }
}
