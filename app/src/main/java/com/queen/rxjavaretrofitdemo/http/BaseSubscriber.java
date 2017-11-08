package com.queen.rxjavaretrofitdemo.http;

import android.content.Context;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * BaseSubscriber
 *
 * @author : Albert
 * @Email : liubinhou007@163.com
 * @date : 2017/9/29
 * @Description :所有订阅者的基类
 */
public class BaseSubscriber<T> extends Subscriber<T> {

    private BaseOnNextListener mBaseOnNextListener;
    private Context mContext;

    public BaseSubscriber(BaseOnNextListener mBaseOnNextListener, Context mContext) {
        this.mBaseOnNextListener = mBaseOnNextListener;
        this.mContext = mContext;
    }



    @Override
    public void onCompleted() {
        Toast.makeText(mContext, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
        unSubscribe();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(mContext, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(mContext, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        unSubscribe();
    }

    @Override
    public void onNext(T t) {
        if (mBaseOnNextListener != null){
            mBaseOnNextListener.onNext(t);
        }
    }

    public void unSubscribe(){
        if (!isUnsubscribed()){
            this.unsubscribe();
        }
    }
}
