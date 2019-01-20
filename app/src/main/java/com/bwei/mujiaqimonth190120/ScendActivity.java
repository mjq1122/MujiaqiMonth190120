package com.bwei.mujiaqimonth190120;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.PicAdapter;
import beans.GoodsDeta;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import contastss.Contasts;
import presenter.MainPresenter;
import view.IView;

public class ScendActivity extends AppCompatActivity implements IView {

    @BindView(R.id.scend_img)
    ImageView scendImg;
    @BindView(R.id.scend_btnFX)
    Button scendBtnFX;
    //@BindView(R.id.scend_viewpager)
    ViewPager scendViewpager;
    @BindView(R.id.scend_title)
    TextView scendTitle;
    @BindView(R.id.scend_price)
    TextView scendPrice;
    @BindView(R.id.scend_btncart)
    Button scendBtncart;
    /*@BindView(R.id.xbanner)
    com.stx.xhb.xbanner.XBanner xbanner;*/
    private MainPresenter presenter = new MainPresenter(this);
    private List<String> PicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scend);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int pid = intent.getIntExtra("pid", 1);
        Map<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        presenter.get(Contasts.GoodsData_url, map, GoodsDeta.class);
    }


    @OnClick({R.id.scend_btnFX, R.id.scend_btncart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scend_btnFX:
                break;
            case R.id.scend_btncart:
                break;
        }
    }

    @Override
    public void Onsuccess(Object o) {
        if (o instanceof GoodsDeta) {
            GoodsDeta goodsDeta = (GoodsDeta) o;
            Log.e("goodsDeta", "Onsuccess: " + goodsDeta.getData().toString());
            scendTitle.setText(goodsDeta.getData().getTitle());
            scendPrice.setText("¥：" + goodsDeta.getData().getPrice());
            //切割图片
            String images = goodsDeta.getData().getImages();//得到图片集
            String[] split = images.split("\\|");//得到一个图片
            if (split.length > 0) {
                //将https成http  进行联网显示
                String replace = split[0].replace("https", "http");
                PicList.add(replace);
            }
            //轮播图
            getPic(PicList);
        }

    }

    private void getPic(final List<String> picList) {
        for (int i = 0; i < picList.size(); i++) {
            Log.e("getPic", "getPic: "+picList);
        }
        scendViewpager.setAdapter(new PicAdapter(picList,this));
        //第二个参数为提示文字资源集合
        /*xbanner.setData(picList, null);
        // XBanner适配数据
        xbanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(ScendActivity.this).load(picList.get(position)).into((ImageView) view);
            }
        });
        xbanner.setPageTransformer(Transformer.Default);//横向移动*/
    }

    @Override
    public void Onerror(String error) {

    }
}
