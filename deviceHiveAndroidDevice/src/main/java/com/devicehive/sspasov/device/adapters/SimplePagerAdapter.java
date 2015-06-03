package com.devicehive.sspasov.device.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.fragments.DeviceCommandsFragment;
import com.devicehive.sspasov.device.fragments.DeviceInformationFragment;
import com.devicehive.sspasov.device.fragments.DeviceSendNotificationFragment;
import com.devicehive.sspasov.device.fragments.EquipmentListFragment;

/**
 * Created by stanimir on 03.06.15.
 */
public class SimplePagerAdapter extends FragmentPagerAdapter {

    private Activity mContext;

    public SimplePagerAdapter(Activity context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 4;
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
                return DeviceInformationFragment.getInstance();
            case 1:
                return EquipmentListFragment.getInstance();
            case 2:
                return DeviceCommandsFragment.getInstance();
            default:
                return DeviceSendNotificationFragment.getInstance();
        }
    }

/*
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view = mContext.getLayoutInflater().inflate(R.layout.pager_item,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);

        // Retrieve a TextView from the inflated View, and update it's text
        TextView title = (TextView) view.findViewById(R.id.item_title);
        title.setText(String.valueOf(position + 1));

        // Return the View
        return view;
    }*/

    /*@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }*/

}