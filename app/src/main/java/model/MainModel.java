package model;

import com.google.gson.Gson;

import java.util.Map;

import api.RetrofitUtils;
import callback.MyCallBack;

/**
 * 作者：穆佳琪
 * 时间：2019/1/20 14:29.
 */

public class MainModel implements IModel {
    @Override
    public void get(String url, Map<String, Object> map, final Class clazz, final MyCallBack callBack) {
        RetrofitUtils.getInstance().get(url, map, new RetrofitUtils.HttpListener() {
            @Override
            public void success(String json) {
                Object o = new Gson().fromJson(json, clazz);
                callBack.onsuccess(o);
            }

            @Override
            public void error(String error) {
                callBack.onerror(error);
            }
        });
    }
}
