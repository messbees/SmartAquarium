package com.messbees.smartaquarium;

import android.content.Context;
import android.provider.Settings.Secure;
/**
 * Created by brijesh on 20/4/17.
 */

public class Constants {

    public static final String MQTT_BROKER_URL = "tcp://db3gw.keenetic.pro:8883";

    public static final String LIGHT_TOPIC = "/light";

    public static final String CLIENT_ID = Secure.getString(getContext().getContentResolver(),Secure.ANDROID_ID);

    public static final String SUBSCRIBE_TOPIC = "/SmartAquarium/ds18b20/temperature";

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context c) {
        context = c;
    }
}

