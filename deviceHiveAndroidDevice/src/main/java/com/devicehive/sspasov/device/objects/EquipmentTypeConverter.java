package com.devicehive.sspasov.device.objects;

/**
 * Created by toni on 30.05.15.
 */
public class EquipmentTypeConverter {
    private static EquipmentTypeConverter instance;

    public static EquipmentTypeConverter getInstance() {
        if(instance == null) {
            instance = new EquipmentTypeConverter();
        }
        return instance;
    }

    private EquipmentTypeConverter() {}

    public static String toString(String type){
        int mType = Integer.decode(type);

        switch (mType) {
            case TYPE_ACCELEROMETER:
                return STRING_TYPE_ACCELEROMETER;
            case TYPE_MAGNETIC_FIELD:
                return STRING_TYPE_MAGNETIC_FIELD;
            case TYPE_ORIENTATION:
                return STRING_TYPE_ORIENTATION;
            case TYPE_GYROSCOPE:
                return STRING_TYPE_GYROSCOPE;
            case TYPE_LIGHT:
                return STRING_TYPE_LIGHT;
            case TYPE_PRESSURE:
                return STRING_TYPE_PRESSURE;
            case TYPE_TEMPERATURE:
                return STRING_TYPE_TEMPERATURE;
            case TYPE_PROXIMITY:
                return STRING_TYPE_PROXIMITY;
            case TYPE_GRAVITY:
                return STRING_TYPE_GRAVITY;
            case TYPE_LINEAR_ACCELERATION:
                return STRING_TYPE_LINEAR_ACCELERATION;
            case TYPE_ROTATION_VECTOR:
                return STRING_TYPE_ROTATION_VECTOR;
            case TYPE_RELATIVE_HUMIDITY:
                return STRING_TYPE_RELATIVE_HUMIDITY;
            case TYPE_AMBIENT_TEMPERATURE:
                return STRING_TYPE_AMBIENT_TEMPERATURE;
            case TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                return STRING_TYPE_MAGNETIC_FIELD_UNCALIBRATED;
            case TYPE_GAME_ROTATION_VECTOR:
                return STRING_TYPE_GAME_ROTATION_VECTOR;
            case TYPE_GYROSCOPE_UNCALIBRATED:
                return STRING_TYPE_GYROSCOPE_UNCALIBRATED;
            case TYPE_SIGNIFICANT_MOTION:
                return STRING_TYPE_SIGNIFICANT_MOTION;
            case TYPE_STEP_DETECTOR:
                return STRING_TYPE_STEP_DETECTOR;
            case TYPE_STEP_COUNTER:
                return STRING_TYPE_STEP_COUNTER;
            case TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                return STRING_TYPE_GEOMAGNETIC_ROTATION_VECTOR;
            case TYPE_HEART_RATE:
                return STRING_TYPE_HEART_RATE;
            case TYPE_TILT_DETECTOR:
                return STRING_TYPE_TILT_DETECTOR;
            case TYPE_WAKE_GESTURE:
                return STRING_TYPE_WAKE_GESTURE;
            case TYPE_GLANCE_GESTURE:
                return STRING_TYPE_GLANCE_GESTURE;
            case TYPE_PICK_UP_GESTURE:
                return STRING_TYPE_PICK_UP_GESTURE;
            default:
                return null;
        }
    }

    public static String toString(int type){

        switch (type) {
            case TYPE_ACCELEROMETER:
                return STRING_TYPE_ACCELEROMETER;
            case TYPE_MAGNETIC_FIELD:
                return STRING_TYPE_MAGNETIC_FIELD;
            case TYPE_ORIENTATION:
                return STRING_TYPE_ORIENTATION;
            case TYPE_GYROSCOPE:
                return STRING_TYPE_GYROSCOPE;
            case TYPE_LIGHT:
                return STRING_TYPE_LIGHT;
            case TYPE_PRESSURE:
                return STRING_TYPE_PRESSURE;
            case TYPE_TEMPERATURE:
                return STRING_TYPE_TEMPERATURE;
            case TYPE_PROXIMITY:
                return STRING_TYPE_PROXIMITY;
            case TYPE_GRAVITY:
                return STRING_TYPE_GRAVITY;
            case TYPE_LINEAR_ACCELERATION:
                return STRING_TYPE_LINEAR_ACCELERATION;
            case TYPE_ROTATION_VECTOR:
                return STRING_TYPE_ROTATION_VECTOR;
            case TYPE_RELATIVE_HUMIDITY:
                return STRING_TYPE_RELATIVE_HUMIDITY;
            case TYPE_AMBIENT_TEMPERATURE:
                return STRING_TYPE_AMBIENT_TEMPERATURE;
            case TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                return STRING_TYPE_MAGNETIC_FIELD_UNCALIBRATED;
            case TYPE_GAME_ROTATION_VECTOR:
                return STRING_TYPE_GAME_ROTATION_VECTOR;
            case TYPE_GYROSCOPE_UNCALIBRATED:
                return STRING_TYPE_GYROSCOPE_UNCALIBRATED;
            case TYPE_SIGNIFICANT_MOTION:
                return STRING_TYPE_SIGNIFICANT_MOTION;
            case TYPE_STEP_DETECTOR:
                return STRING_TYPE_STEP_DETECTOR;
            case TYPE_STEP_COUNTER:
                return STRING_TYPE_STEP_COUNTER;
            case TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                return STRING_TYPE_GEOMAGNETIC_ROTATION_VECTOR;
            case TYPE_HEART_RATE:
                return STRING_TYPE_HEART_RATE;
            case TYPE_TILT_DETECTOR:
                return STRING_TYPE_TILT_DETECTOR;
            case TYPE_WAKE_GESTURE:
                return STRING_TYPE_WAKE_GESTURE;
            case TYPE_GLANCE_GESTURE:
                return STRING_TYPE_GLANCE_GESTURE;
            case TYPE_PICK_UP_GESTURE:
                return STRING_TYPE_PICK_UP_GESTURE;
            default:
                return null;
        }
    }

    private static final int TYPE_ACCELEROMETER = 1;
    private static final String STRING_TYPE_ACCELEROMETER = "accelerometer";

    private static final int TYPE_MAGNETIC_FIELD = 2;
    private static final String STRING_TYPE_MAGNETIC_FIELD = "magnetic_field";

    @Deprecated
    private static final int TYPE_ORIENTATION = 3;
    @Deprecated
    private static final String STRING_TYPE_ORIENTATION = "orientation";

    private static final int TYPE_GYROSCOPE = 4;
    private static final String STRING_TYPE_GYROSCOPE = "gyroscope";

    private static final int TYPE_LIGHT = 5;
    private static final String STRING_TYPE_LIGHT = "light";

    private static final int TYPE_PRESSURE = 6;
    private static final String STRING_TYPE_PRESSURE = "pressure";

    @Deprecated
    private static final int TYPE_TEMPERATURE = 7;
    @Deprecated
    private static final String STRING_TYPE_TEMPERATURE = "temperature";

    private static final int TYPE_PROXIMITY = 8;
    private static final String STRING_TYPE_PROXIMITY = "proximity";

    private static final int TYPE_GRAVITY = 9;
    private static final String STRING_TYPE_GRAVITY = "gravity";

    private static final int TYPE_LINEAR_ACCELERATION = 10;
    private static final String STRING_TYPE_LINEAR_ACCELERATION = "linear_acceleration";

    private static final int TYPE_ROTATION_VECTOR = 11;
    private static final String STRING_TYPE_ROTATION_VECTOR = "rotation_vector";

    private static final int TYPE_RELATIVE_HUMIDITY = 12;
    private static final String STRING_TYPE_RELATIVE_HUMIDITY = "relative_humidity";

    private static final int TYPE_AMBIENT_TEMPERATURE = 13;
    private static final String STRING_TYPE_AMBIENT_TEMPERATURE = "ambient_temperature";

    private static final int TYPE_MAGNETIC_FIELD_UNCALIBRATED = 14;
    private static final String STRING_TYPE_MAGNETIC_FIELD_UNCALIBRATED = "magnetic_field_uncalibrated";

    private static final int TYPE_GAME_ROTATION_VECTOR = 15;
    private static final String STRING_TYPE_GAME_ROTATION_VECTOR = "game_rotation_vector";

    private static final int TYPE_GYROSCOPE_UNCALIBRATED = 16;
    private static final String STRING_TYPE_GYROSCOPE_UNCALIBRATED = "gyroscope_uncalibrated";

    private static final int TYPE_SIGNIFICANT_MOTION = 17;
    private static final String STRING_TYPE_SIGNIFICANT_MOTION = "significant_motion";

    private static final int TYPE_STEP_DETECTOR = 18;
    private static final String STRING_TYPE_STEP_DETECTOR = "step_detector";

    private static final int TYPE_STEP_COUNTER = 19;
    private static final String STRING_TYPE_STEP_COUNTER = "step_counter";

    private static final int TYPE_GEOMAGNETIC_ROTATION_VECTOR = 20;
    private static final String STRING_TYPE_GEOMAGNETIC_ROTATION_VECTOR = "geomagnetic_rotation_vector";

    private static final int TYPE_HEART_RATE = 21;
    private static final String STRING_TYPE_HEART_RATE = "heart_rate";

    private static final int TYPE_TILT_DETECTOR = 22;
    private static final String STRING_TYPE_TILT_DETECTOR = "tilt_detector";

    private static final int TYPE_WAKE_GESTURE = 23;
    private static final String STRING_TYPE_WAKE_GESTURE = "wake_gesture";

    private static final int TYPE_GLANCE_GESTURE = 24;
    private static final String STRING_TYPE_GLANCE_GESTURE = "glance_gesture";

    private static final int TYPE_PICK_UP_GESTURE = 25;
    private static final String STRING_TYPE_PICK_UP_GESTURE = "pick_up_gesture";
}
