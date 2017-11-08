package com.queen.rxjavaretrofitdemo.http;

/**
 * BaseOnNextListener
 *
 * @author : Albert
 * @Email : liubinhou007@163.com
 * @date : 2017/9/29
 * @Description :网路请求onNext处理基类接口
 */
public interface BaseOnNextListener<T> {
    void onNext(T t);
}
