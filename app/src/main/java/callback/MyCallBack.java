package callback;

/**
 * 作者：穆佳琪
 * 时间：2019/1/20 14:27.
 */

public interface MyCallBack<T> {
    void onsuccess(T t);
    void onerror(String error);
}
