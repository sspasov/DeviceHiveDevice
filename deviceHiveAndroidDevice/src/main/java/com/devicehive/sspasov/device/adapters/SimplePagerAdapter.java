package com.devicehive.sspasov.device.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.ui.fragments.DeviceCommandsFragment;
import com.devicehive.sspasov.device.ui.fragments.DeviceInformationFragment;
import com.devicehive.sspasov.device.ui.fragments.DeviceSendNotificationFragment;
import com.devicehive.sspasov.device.ui.fragments.EquipmentListFragment;
import com.devicehive.sspasov.device.utils.L;

/**
 * Created by stanimir on 03.06.15.
 */
public class SimplePagerAdapter extends FragmentStatePagerAdapter {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private static final String TAG = SimplePagerAdapter.class.getSimpleName();
    private static int NUM_ITEMS = 4;

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private Context mContext;

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public SimplePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
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
                return DeviceInformationFragment.getInstance();
            case 1:
                L.d(TAG, "EquipmentListFragment.getInstance();");
                return EquipmentListFragment.getInstance();
            case 2:
                L.d(TAG, "DeviceCommandsFragment.getInstance();");
                return DeviceCommandsFragment.getInstance();
            default:
                L.d(TAG, "DeviceSendNotificationFragment.getInstance();");
                return DeviceSendNotificationFragment.getInstance();
        }
    }
}