package com.devicehive.sspasov.device.config;

public final class DeviceHiveConfig {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    public static final boolean DEFAULT_FIRST_STARTUP = true;

    /**
     * General
     */
    public static final String DEFAULT_API_ENDPOINT = null;

    /**
     * Network
     */
    public static final String DEFAULT_NETWORK_NAME = "Simple Device Network";
    public static final String DEFAULT_NETWORK_DESCRIPTION = "no network description";

    /**
     * Optional
     */
    public static final int DEFAULT_DEVICE_TIMEOUT = 3600;
    public static final int DEFAULT_DEVICE_MIN_TIMEOUT = 0;
    public static final int DEFAULT_DEVICE_MAX_TIMEOUT = 3600;
    public static final boolean DEFAULT_DEVICE_ASYNC_COMMAND_EXECUTION = false;
    public static final boolean DEFAULT_DEVICE_IS_PERMANENT = false;

}
