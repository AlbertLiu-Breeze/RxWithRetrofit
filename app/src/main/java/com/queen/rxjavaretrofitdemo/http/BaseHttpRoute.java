package com.queen.rxjavaretrofitdemo.http;

import android.util.Log;

import com.queen.rxjavaretrofitdemo.entity.HttpResult;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * BaseHttpRoute
 *
 * @author : Albert
 * @Email : liubinhou007@163.com
 * @date : 2017/9/28
 * @Description :最基础的网络请求路由
 */
public class BaseHttpRoute {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    public static final int DEFAULT_TIMEOUT = 5;

    public Retrofit retrofit;
    public MovieService movieService;


    public BaseHttpRoute() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request custom = request.newBuilder()
                                .header("User-Agent","Albert")
                                .header("test","test")
                                .method(request.method(),request.body())
                                .build();
                        Log.e("liubinhou","header = " + custom.headers().toString());
                        Log.e("liubinhou","method = " + request.method() + "  url = " + request.url());
                        return chain.proceed(custom);
                    }
                });



        retrofit = new Retrofit.Builder()
                .client(builder.build())
                //modify by zqikai 20160317 for 对http请求结果进行统一的预处理 GosnResponseBodyConvert
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ResponseConvertFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    public  <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }



    /**
     * TODO 应该将所有的model数据都进行传递，这样会根据返回的状态码进行不同的逻辑处理
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCount() == 0) {
                throw new ApiException(100);
            }

            return httpResult.getSubjects();
        }
    }
}
