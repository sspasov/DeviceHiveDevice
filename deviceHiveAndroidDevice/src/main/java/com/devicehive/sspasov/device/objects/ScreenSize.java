package com.devicehive.sspasov.device.objects;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by toni on 28.05.15.
 */
public class ScreenSize {

    private static final String TAG = ScreenSize.class.getSimpleName();
    private final String NAME = "value";
    private String value;
    private Context mContext;

    private static ScreenSize instance;

    public static ScreenSize getInstance() {
        if(instance == null) {
            instance = new ScreenSize();
        }
        return instance;
    }

    public static ScreenSize getInstance(Context context) {
        if(instance == null) {
            instance = new ScreenSize(context);
        }
        return instance;
    }

    public ScreenSize() {}


    public ScreenSize(Context context) {
        this.mContext = context;
        this.value = "0";
    }

    public void setContext(Context context) {
        this.mContext = context;

    }

    public String getValue() {
        if(mContext == null) {
            Log.e(TAG, "You must setContext() before getValue");
            return "null";
        }
        return getScreenSize();
    }

    private String getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double)dens;
        double hi = (double)height / (double)dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x+y);
        return String.valueOf(screenInches);
    }

    public String getName() {
        return NAME;
    }








}
