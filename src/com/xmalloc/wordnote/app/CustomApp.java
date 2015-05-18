package com.xmalloc.wordnote.app;

import android.app.Application;
import android.telephony.TelephonyManager;
/**
 * Created by zhch on 2015/5/18.
 */
public class CustomApp  extends Application{
    private static CustomApp s_instance;

    private String deviceId;

    public static CustomApp getInstance() {
        return s_instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        s_instance = this;

    }

    /**
     * 设备标识
     * @return
     */
    public String getDeviceId() {
        if (null == deviceId) {
            TelephonyManager tm = (TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
            if (null == deviceId) {
                deviceId = tm.getSubscriberId();
            }
        }
        return deviceId;
    }
}
