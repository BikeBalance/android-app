package me.borisbike.android.helpers;

/**
 * Created by mani on 11/10/14.
 */

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class AndroidID {
    Context context;

    public AndroidID(Context ctx){
        this.context = ctx;
    }
    public String getAndroidID(){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }
}

