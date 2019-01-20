package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwei.mujiaqimonth190120.R;

import java.util.List;

import beans.GoodsBean;

/**
 * 作者：穆佳琪
 * 时间：2019/1/20 14:54.
 */

public class GoodsBeanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GoodsBean.DataBean> list;
    private Context context;

    public GoodsBeanAdapter(List<GoodsBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_item_layout, parent, false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ViewHodel)holder).goods_title.setText(list.get(position).getTitle());
        ((ViewHodel)holder).goods_price.setText("¥："+list.get(position).getPrice());

        //切割图片
        String images = list.get(position).getImages();//得到图片集
        String[] split = images.split("\\|");//得到一个图片
        if (split.length>0) {
            //将https成http  进行联网显示
            String replace = split[0].replace("https", "http");
            Uri parse = Uri.parse(replace);
            Glide.with(context).load(replace).into(((ViewHodel)holder).goods_img);//设置图片
        }

        ((ViewHodel)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(list.get(position).getPid());
            }
        });
        ((ViewHodel)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.OnItemLongClick(v,position);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ViewHodel extends RecyclerView.ViewHolder {
        ImageView goods_img;
        TextView goods_title,goods_price;
        public ViewHodel(View view) {
            super(view);
            goods_img=view.findViewById(R.id.goods_img);
            goods_title=view.findViewById(R.id.goods_title);
            goods_price=view.findViewById(R.id.goods_price);
        }
    }

    //添加监听
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener{
        void OnItemLongClick(View v,int position);
    }

}
