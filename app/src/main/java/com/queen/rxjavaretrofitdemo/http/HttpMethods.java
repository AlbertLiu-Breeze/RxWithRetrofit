package com.queen.rxjavaretrofitdemo.http;

import com.queen.rxjavaretrofitdemo.entity.Subject;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by liukun on 16/3/9.
 */
public class HttpMethods extends BaseHttpRoute{

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     * @param subscriber  由调用者传过来的观察者对象
     * @param start 起始位置
     * @param count 获取长度
     */
    public void getTopMovie(Subscriber<List<Subject>> subscriber, int start, int count){

        Observable observable = movieService.getTopMovie(start, count)
                .map(new HttpResultFunc<List<Subject>>());


        toSubscribe(observable, subscriber);
    }


}
