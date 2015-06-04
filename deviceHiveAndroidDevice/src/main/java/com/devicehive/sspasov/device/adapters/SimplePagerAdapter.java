package com.devicehive.sspasov.device.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dataart.android.devicehive.Command;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.fragments.DeviceCommandsFragment;
import com.devicehive.sspasov.device.fragments.DeviceInformationFragment;
import com.devicehive.sspasov.device.fragments.DeviceSendNotificationFragment;
import com.devicehive.sspasov.device.fragments.EquipmentListFragment;
import com.devicehive.sspasov.device.objects.TestDevice;
import com.devicehive.sspasov.device.utils.L;

import java.util.List;

/**
 * Created by stanimir on 03.06.15.
 */
public class SimplePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = SimplePagerAdapter.class.getSimpleName();

    private static int NUM_ITEMS = 4;
    private Context mContext;
    private TestDevice mDevice;
    private List<Command> mReceivedCommands;
    private DeviceSendNotificationFragment.ParameterProvider mParameterProvider;
    private DeviceSendNotificationFragment.Parameter param = null;

    public SimplePagerAdapter(Context context, FragmentManager fragmentManager, TestDevice device, List<Command> receivedCommands, DeviceSendNotificationFragment.ParameterProvider parameterProvider) {
        super(fragmentManager);
        mContext = context;
        mDevice = device;
        mReceivedCommands = receivedCommands;
        mParameterProvider = parameterProvider;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_device_info);
            case 1:
                return mContext.getString(R.string.tab_device_equipment);
            case 2:
                return mContext.getString(R.string.tab_device_commands);
            default:
                return mContext.getString(R.string.tab_device_send_notification);
        }
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                L.d(TAG, "DeviceInformationFragment.getInstance();");
                DeviceInformationFragment deviceInformationFragment = DeviceInformationFragment.newInstance();
                deviceInformationFragment.setDeviceData(mDevice.getDeviceData());
                return deviceInformationFragment;
            case 1:
                L.d(TAG, "EquipmentListFragment.getInstance();");
                EquipmentListFragment equipmentListFragment = EquipmentListFragment.newInstance();
                equipmentListFragment.setEquipment(mDevice.getDeviceData().getEquipment());
                return equipmentListFragment;
            case 2:
                L.d(TAG, "DeviceCommandsFragment.getInstance();");
                DeviceCommandsFragment deviceCommandsFragment = DeviceCommandsFragment.newInstance();
                deviceCommandsFragment.setCommands(mReceivedCommands);
                return deviceCommandsFragment;
            default:
                L.d(TAG, "DeviceSendNotificationFragment.getInstance();");
                DeviceSendNotificationFragment deviceSendNotificationFragment = DeviceSendNotificationFragment.newInstance();
                deviceSendNotificationFragment.setParameterProvider(mParameterProvider);
                deviceSendNotificationFragment.setEquipment(mDevice.getDeviceData().getEquipment());
                //TODO: bugged not working
                if (param != null) {
                    deviceSendNotificationFragment.addParameter(param.name, param.value);
                    param = null;
                }
                return deviceSendNotificationFragment;
        }
    }

    //TODO: bugged not working
    public void addParameter(String name, String value) {
        param = new DeviceSendNotificationFragment.Parameter(name, value);
    }
}