package presenter;

import java.util.Map;

import callback.MyCallBack;
import model.MainModel;
import view.IView;

/**
 * 作者：穆佳琪
 * 时间：2019/1/20 14:31.
 */

public class MainPresenter implements IPresenter {
    private IView view;
    private MainModel mainModel;

    public MainPresenter(IView view) {
        this.view = view;
        mainModel = new MainModel();
    }

    @Override
    public void get(String url, Map<String, Object> map, Class clazz) {
        mainModel.get(url, map, clazz, new MyCallBack() {
            @Override
            public void onsuccess(Object o) {
                if(view!=null){
                    view.Onsuccess(o);
                }
            }

            @Override
            public void onerror(String error) {
                if(view!=null){
                    view.Onerror(error);
                }
            }
        });
    }
}
