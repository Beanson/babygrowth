package com.example.administrator.babygrowth01.babyparadise.clothesMall;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class ClothesListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<ClothesJson> arrayList;
    private Activity activity;

    public ClothesListRecycleAdapter(ArrayList<ClothesJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.clothes_buy_each,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        /* binding the id as tag with the clothes linearlayout */
        vh.getLy_clothes_01().setTag(arrayList.get(position).getClothes_01().getId());
        vh.getLy_clothes_02().setTag(arrayList.get(position).getClothes_02().getId());

        /* bind other message to view*/
        /* clothes thumbnail*/
        Picasso.with(activity).load(arrayList.get(position).getClothes_01().getThumbnail_uri()).into(vh.getIv_clothes_01());
        Picasso.with(activity).load(arrayList.get(position).getClothes_02().getThumbnail_uri()).into(vh.getIv_clothes_02());

        /* clothes depict*/
        vh.getTv_clothes_01().setText(arrayList.get(position).getClothes_01().getDepict());
        vh.getTv_clothes_02().setText(arrayList.get(position).getClothes_02().getDepict());

        /* clothes price*/
        vh.getTv_price_01().setText("¥ "+arrayList.get(position).getClothes_01().getPrice());
        vh.getTv_price_02().setText("¥ "+arrayList.get(position).getClothes_02().getPrice());

        /* clothes age layer*/
        vh.getTv_age_01().setText(arrayList.get(position).getClothes_01().getAge_layer()+"岁");
        vh.getTv_age_02().setText(arrayList.get(position).getClothes_02().getAge_layer()+"岁");

        /* clothes month sell*/
        vh.getTv_month_sell_01().setText("月销: "+arrayList.get(position).getClothes_01().getMonth_sell());
        vh.getTv_month_sell_02().setText("月销: "+arrayList.get(position).getClothes_02().getMonth_sell());
    }


    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private LinearLayout ly_clothes_01,ly_clothes_02;
        private ImageView iv_clothes_01,iv_clothes_02; //clothes image
        private TextView tv_clothes_01,tv_clothes_02; //clothes depict
        private TextView tv_price_01,tv_price_02; //clothes price
        private TextView tv_age_01,tv_age_02; // proper age layer
        private TextView tv_month_sell_01,tv_month_sell_02; //sell per month

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            iv_clothes_01= (ImageView) itemView.findViewById(R.id.iv_clothes_01);
            iv_clothes_02= (ImageView) itemView.findViewById(R.id.iv_clothes_02);
            tv_clothes_01= (TextView) itemView.findViewById(R.id.tv_clothes_01);
            tv_clothes_02= (TextView) itemView.findViewById(R.id.tv_clothes_02);
            tv_price_01= (TextView) itemView.findViewById(R.id.tv_price_01);
            tv_price_02= (TextView) itemView.findViewById(R.id.tv_price_02);
            tv_age_01= (TextView) itemView.findViewById(R.id.tv_age_01);
            tv_age_02= (TextView) itemView.findViewById(R.id.tv_age_02);
            tv_month_sell_01= (TextView) itemView.findViewById(R.id.tv_month_sell_01);
            tv_month_sell_02= (TextView) itemView.findViewById(R.id.tv_month_sell_02);
            ly_clothes_01= (LinearLayout) itemView.findViewById(R.id.ly_clothes_01);
            ly_clothes_02= (LinearLayout) itemView.findViewById(R.id.ly_clothes_02);
        }

        public ImageView getIv_clothes_01() {
            return iv_clothes_01;
        }

        public ImageView getIv_clothes_02() {
            return iv_clothes_02;
        }

        public TextView getTv_clothes_01() {
            return tv_clothes_01;
        }

        public TextView getTv_clothes_02() {
            return tv_clothes_02;
        }

        public TextView getTv_price_01() {
            return tv_price_01;
        }

        public TextView getTv_price_02() {
            return tv_price_02;
        }

        public TextView getTv_age_01() {
            return tv_age_01;
        }

        public TextView getTv_age_02() {
            return tv_age_02;
        }

        public TextView getTv_month_sell_01() {
            return tv_month_sell_01;
        }

        public TextView getTv_month_sell_02() {
            return tv_month_sell_02;
        }

        public LinearLayout getLy_clothes_01() {
            return ly_clothes_01;
        }

        public LinearLayout getLy_clothes_02() {
            return ly_clothes_02;
        }
    }
}
