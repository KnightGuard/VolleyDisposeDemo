package com.example.vollryhttpdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * 获取手机的设备号
 * Created by AndyON on 2015/9/9.
 */
public class DeviceUuid {

    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static UUID uuid;
    private static final String SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME";

    public DeviceUuid(Context context) {
        synchronized (DeviceUuid.class) {
            if (uuid == null) {
                final SharedPreferences prefs = context.getSharedPreferences(
                        SHARED_PREFERENCE_NAME, 0);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);
                if (id != null) {
                    uuid = UUID.fromString(id);
                } else {
                    // ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
                    final String androidId = Settings.Secure.getString(
                            context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    try {
                        // 在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId
                                    .getBytes("utf8"));
                        } else {
                            // 非手机设备：
                            // 如果只带有Wifi的设备或者音乐播放器没有通话的硬件功能的话就没有这个DEVICE_ID
                            final String deviceId = ((TelephonyManager) context
                                    .getSystemService(Context.TELEPHONY_SERVICE))
                                    .getDeviceId();
                            uuid = deviceId != null ? UUID
                                    .nameUUIDFromBytes(deviceId
                                            .getBytes("utf8")) : UUID
                                    .randomUUID();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString())
                            .commit();
                }
            }
        }
    }

    /**
     * 获取APP版本号
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    "com.shboka.beautyorder", 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取系统版本号例如：4.2
     */
    public static String getVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 返回设备唯一的 id, 缺点：1 、对于Android 2.2（“Froyo”）之前的设备不是100％的可靠<br>
     * 2、此外，在主流制造商的畅销手机中至少存在一个众所周知的错误，每一个实例都具有相同的ANDROID_ID。<br>
     */
    @Deprecated
    public static String getIosDeviceId(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public String getDeviceUuidStr() {
        return uuid.toString();
    }

    /**
     * 返回设备唯一编码
     */
    public static String getDeviceUuidStr(Context context) {

        String strUUID = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE).getString(PREFS_DEVICE_ID, "");
        if (isNull(strUUID)) {
            return new DeviceUuid(context).getDeviceUuidStr();
        }
        // Log.e("uuid", strUUID);
        return strUUID;
    }

    /**
     * 检查传入的str是否是null，"","null"
     */
    public static boolean isNull(String str) {
        if (str == null || "".equals(str.trim())
                || "null".equalsIgnoreCase(str.trim()))
            return true;
        return false;
    }
}
