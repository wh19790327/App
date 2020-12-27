package com.example.listenercollection.retrofit;

import androidx.annotation.Nullable;

import com.darkhorse.httphelper.converter.BaseConvert;
import com.example.listenercollection.retrofit.response.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyConverter implements BaseConvert {
    @Nullable
    @Override
    public RequestBody beanToRequest(Gson gson, TypeAdapter<?> typeAdapter, Object o) {
        return null;
    }

    @Nullable
    @Override
    public Object responseToBean(Gson gson, TypeAdapter<?> typeAdapter, ResponseBody responseBody) {
        BaseResponse bean = null;
        try {
            String originalBody = responseBody.string();

            bean = (BaseResponse) typeAdapter.fromJson(originalBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
