package com.example.listenercollection.retrofit;

import com.darkhorse.baseframe.okhttp.HttpHelper;

public class API {
    public static final String baseUrl = "http://www.zxyxy.vip/";
    private MyService mService;

    public MyService getService() {
        if (mService == null) {
            mService = HttpHelper.INSTANCE.getService(MyService.class);
        }
        return mService;
    }

    public static API getInstance() {
        return Holder.mSingleton;
    }

    private static class Holder {
        static API mSingleton = new API();
    }
}

