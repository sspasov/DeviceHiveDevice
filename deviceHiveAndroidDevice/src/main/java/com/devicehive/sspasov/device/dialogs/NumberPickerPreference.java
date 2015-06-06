package com.devicehive.sspasov.device.dialogs;

/**
 * Created by toni on 31.05.15.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.devicehive.sspasov.device.config.DeviceHiveConfig;

public class NumberPickerPreference extends DialogPreference {
    private NumberPicker mPicker;
    private EditText mPickerOld;
    private int mNumber = 0;

    public NumberPickerPreference(Context context) {
        this(context, null, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView title = (TextView) view.findViewById(android.R.id.title);
        if (title != null) {
            title.setSingleLine(false);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        }
    }

    @Override
    protected View onCreateDialogView() {
        if (Build.VERSION.SDK_INT >= 11) {
            mPicker = new NumberPicker(getContext());
            mPicker.setMinValue(DeviceHiveConfig.DEFAULT_DEVICE_MIN_TIMEOUT);
            mPicker.setMaxValue(DeviceHiveConfig.DEFAULT_DEVICE_MAX_TIMEOUT);
            // should be after setMinValue and setMaxValue calls
            mPicker.setValue(mNumber);
            return mPicker;
        } else {
            mPickerOld = new EditText(getContext());
            mPickerOld.setText(mNumber + "");
            return mPickerOld;
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // needed when user edits the text field and clicks OK
            if (Build.VERSION.SDK_INT >= 11) {
                mPicker.clearFocus();
                setValue(mPicker.getValue());
            } else {
                mPickerOld.clearFocus();
                setValue(Integer.valueOf(mPickerOld.getText().toString()));
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mNumber) : (Integer) defaultValue);
    }

    public void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }

        if (value != mNumber) {
            mNumber = value;
            notifyChanged();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }
}