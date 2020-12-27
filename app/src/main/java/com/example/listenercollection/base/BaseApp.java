package com.example.listenercollection.base;

import android.app.Application;
import android.os.Build;

import com.darkhorse.baseframe.base.BaseApplication;
import com.darkhorse.baseframe.okhttp.HttpHelper;
import com.darkhorse.baseframe.utils.AppManager;
import com.darkhorse.baseframe.utils.PermissionsUtils;
import com.darkhorse.baseframe.utils.SPUtils;
import com.example.listenercollection.DBUtils;
import com.example.listenercollection.constants.GlobalVars;
import com.example.listenercollection.retrofit.API;
import com.example.listenercollection.retrofit.MyConverter;

import java.util.UUID;

import okhttp3.logging.HttpLoggingInterceptor;

public class BaseApp extends BaseApplication {
    @Override
    public void initUtils() {
        AppManager.INSTANCE.init(this);
        HttpHelper.INSTANCE.addBaseUrl(API.baseUrl)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .setConvert(new MyConverter())
                .init();
        GlobalVars.setDeviceId(getDeviceId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                DBUtils.getData();
            }
        }).start();
    }

    public static String getDeviceId() {
        String serial = null;
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

}
