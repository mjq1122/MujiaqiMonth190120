package model;

import java.util.Map;

import callback.MyCallBack;

/**
 * 作者：穆佳琪
 * 时间：2019/1/20 14:28.
 */

public interface IModel {
    void get(String url, Map<String ,Object> map,Class clazz, MyCallBack callBack);

}
