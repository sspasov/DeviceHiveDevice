package com.devicehive.sspasov.device.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.fragments.DeviceCommandsFragment;
import com.devicehive.sspasov.device.fragments.DeviceInformationFragment;
import com.devicehive.sspasov.device.fragments.DeviceSendNotificationFragment;
import com.devicehive.sspasov.device.fragments.EquipmentListFragment;
import com.devicehive.sspasov.device.utils.L;

/**
 * Created by stanimir on 03.06.15.
 */
public class SimplePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = SimplePagerAdapter.class.getSimpleName();

    private static int NUM_ITEMS = 4;
    private Activity mContext;

    public SimplePagerAdapter(Activity context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
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