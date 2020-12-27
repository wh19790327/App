package com.example.listenercollection.constants;

import com.darkhorse.baseframe.utils.SPUtils;

public class GlobalVars {

    public static boolean getAuthority() {
        return SPUtils.INSTANCE.getBoolean("authority", false);
    }

    public static void setAuthority(int authority) {
        if(authority==0){
            SPUtils.INSTANCE.put("authority", false);
        }else{
            SPUtils.INSTANCE.put("authority", true);
        }
    }

    public static String getBindId(){
        return SPUtils.INSTANCE.getString("bindId", "");
    }

    public static void setBindId(String bindId){
        SPUtils.INSTANCE.put("bindId", bindId);
    }

    public static String getDeviceId(){
        return SPUtils.INSTANCE.getString("deviceId", "");
    }

    public static void setDeviceId(String bindId){
        SPUtils.INSTANCE.put("deviceId", bindId);
    }
}
