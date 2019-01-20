package com.bwei.mujiaqimonth190120;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.GoodsBeanAdapter;
import beans.GoodsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import contastss.Contasts;
import presenter.MainPresenter;
import view.IView;

public class MainActivity extends AppCompatActivity implements IView {

    @BindView(R.id.main_textDw)
    TextView mainTextDw;
    @BindView(R.id.main_btnDw)
    Button mainBtnDw;
    @BindView(R.id.main_recycler)
    RecyclerView mainRecycler;
    private MainPresenter presenter = new MainPresenter(this);
    private AMapLocationClient mapLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //请求数据
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", "笔记本");
        map.put("page", 1);
        presenter.get(Contasts.GoodsBean_url, map, GoodsBean.class);


        //声明AMapLocationClient类对象
        //初始化定位
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        //异步获取定位结果
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        mainTextDw.setText(amapLocation.getAddress());
                        mapLocationClient.stopLocation();
                    }
                }
            }
        };
        //设置定位回调监听
        mapLocationClient.setLocationListener(mAMapLocationListener);
    }

    @OnClick(R.id.main_btnDw)
    public void onViewClicked() {
        //启动定位
        mapLocationClient.startLocation();
    }



    @Override
    public void Onsuccess(Object o) {
        if (o instanceof GoodsBean) {
            GoodsBean goodsBean= (GoodsBean) o;
            Log.e("GoodsBean", "Onsuccess: "+goodsBean.getData().get(0).toString());
            final List<GoodsBean.DataBean> list = goodsBean.getData();
            final GoodsBeanAdapter goodsBeanAdapter=new GoodsBeanAdapter(list,this);
            mainRecycler.setLayoutManager(new LinearLayoutManager(this));
            mainRecycler.setAdapter(goodsBeanAdapter);
            goodsBeanAdapter.setOnItemClickListener(new GoodsBeanAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {
                    Intent intent = new Intent(MainActivity.this, ScendActivity.class);
                    intent.putExtra("pid",position);
                    startActivity(intent);
                }
            });
            goodsBeanAdapter.setOnItemLongClickListener(new GoodsBeanAdapter.OnItemLongClickListener() {
                @Override
                public void OnItemLongClick(final View v, final int position) {
                    //旋转180度
                    ObjectAnimator ani1 = ObjectAnimator.ofFloat(v,"rotation", 0f,-10f,0f);
                    //透明度变换
                    ObjectAnimator ani2 = ObjectAnimator.ofFloat(v, "alpha", 1f,0f,0.8f);
                    AnimatorSet as = new AnimatorSet();
                    as.playTogether(ani1,ani2);
                    as.setDuration(3000);
                    as.start();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("删除信息...");
                    builder.setMessage("确定要删除吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(position);
                            goodsBeanAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                }
            });
        }
    }

    @Override
    public void Onerror(String error) {

    }
}
