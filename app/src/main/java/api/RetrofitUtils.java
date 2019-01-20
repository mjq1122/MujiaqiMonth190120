package api;

import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import contastss.Contasts;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：穆佳琪
 * 时间：2019/1/20 14:13.
 */

public class RetrofitUtils {

    private MyApiService myApiService;

    private RetrofitUtils() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.DAYS)
                .connectTimeout(20, TimeUnit.DAYS)
                .writeTimeout(20, TimeUnit.DAYS)
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(Contasts.Z_url)
                .build();
        myApiService = retrofit.create(MyApiService.class);
    }

    //单例模式
    public static RetrofitUtils getInstance() {
        return RetroHolder.retro;
    }
    private static class RetroHolder {
        private static RetrofitUtils retro = new RetrofitUtils();
    }

    //get请求
    public void get(String url, Map<String, Object> map, final HttpListener listener) {
        Observer observer = new Observer<ResponseBody>() {

            @Override
            public void onCompleted() {
                Log.e("onCompleted", "get_onCompleted");
            }

            //网络处理失败
            @Override
            public void onError(Throwable e) {
                Log.e("onError", "get_onError" + e.getMessage());
                if (listener != null) {
                    listener.error(e.getMessage());
                }
            }

            //网络处理成功
            @Override
            public void onNext(ResponseBody responseBody) {
                Log.d("onNext", "get_onNext");
                if (listener != null) {
                    try {
                        listener.success(responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        myApiService.get(url, map)
                .subscribeOn(Schedulers.io())//io就是子线程
                //在主线程调用
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public interface HttpListener {
        void success(String json);
        void error(String error);
    }

}
