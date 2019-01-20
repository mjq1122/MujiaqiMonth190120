package view;

/**
 * 作者：穆佳琪
 * 时间：2019/1/20 14:28.
 */

public interface IView<T> {
    void Onsuccess(T t);
    void Onerror(String error);
}
