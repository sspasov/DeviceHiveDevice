package com.devicehive.sspasov.device.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by toni on 30.05.15.
 */
public class TestEquipmentData implements Parcelable {

    private int mData;


    public TestEquipmentData(int mData) {
        this.mData = mData;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<TestEquipmentData> CREATOR
            = new Parcelable.Creator<TestEquipmentData>() {
        public TestEquipmentData createFromParcel(Parcel in) {
            return new TestEquipmentData(in);
        }

        public TestEquipmentData[] newArray(int size) {
            return new TestEquipmentData[size];
        }
    };

    private TestEquipmentData(Parcel in) {
        mData = in.readInt();
    }
}
