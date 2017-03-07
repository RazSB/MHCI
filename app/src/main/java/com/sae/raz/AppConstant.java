package com.sae.raz;


public class AppConstant {
    /*
     * Variables
     */
    public static final int SPLASH_TIME = 1;    // second
    public static final int PASSWORD_LENGTH = 4;
    public static final int AVATAR_SIZE = 256;

    public static final long BEACON_ELAPSED_TIME = 300000;

    public static final float RATIO_DIS = 28.3465f;

    public static final int TYPE_IBEACON = 0;
    public static final int TYPE_EDDYSTONE_URL = 1;
    public static final int TYPE_EDDYSTONE_UID = 2;
    public static final int TYPE_EDDYSTONE_TLM = 3;

    public static final int SCAN_NOTIFICATION_PERIOD = 5000;    // millisecond
    public static final int SCAN_HOME_PERIOD = 2500;    // millisecond
    public static final int SCAN_DURATION = 2000;  // millisecond

    /*
     * Request code
     */


    /*
     * Extra keys
     */
    public static final String EK_BEACON_NUMBER = "EK_BEACON_NUMBER";
    public static final String EK_BEACON_TITLE = "EK_BEACON_TITLE";
    public static final String EK_FROM_NOTIFICATION = "EK_FROM_NOTIFICATION";

    public static final String EK_URL = "EK_URL";
    public static final String EK_TITLE = "EK_TITLE";
}
