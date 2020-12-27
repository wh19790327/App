package com.example.listenercollection.retrofit;

import com.example.listenercollection.retrofit.response.BaseResponse;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HttpObserver implements Observer<BaseResponse> {

    public abstract void onSuccess(String msg);

    public abstract void onFailure(String msg);

    @Override
    public void onNext(BaseResponse tBaseResponse) {
        if (tBaseResponse.getResult().equals(BaseResponse.SUCCESS)) {
            onSuccess(tBaseResponse.getErr_msg());
        } else {
            onFailure(tBaseResponse.getErr_msg());
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        onFailure(e.getMessage());
    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}
