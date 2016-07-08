package com.example.administrator.babygrowth01.familydish;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.familydish.DishStatic;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class DishAddressListRecycleAdapter extends RecyclerView.Adapter {

    private Activity activity;

    public DishAddressListRecycleAdapter( Activity activity) {
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.family_dish_address_dish_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        vh.getTv_order_dish_each().setText(DishStatic.dishSelectedTemps.get(position).getDish_name());
        Picasso.with(activity).load(DishStatic.dishSelectedTemps.get(position).getThumbnail_path()).into(vh.getIv_order_dish_each());
    }

    @Override
    public int getItemCount() {
        return DishStatic.dishSelectedTemps.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private ImageView iv_order_dish_each;
        private TextView tv_order_dish_each;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;

            // imageview and textview find and init
            iv_order_dish_each= (ImageView) itemView.findViewById(R.id.iv_order_dish_each);
            tv_order_dish_each= (TextView) itemView.findViewById(R.id.tv_order_dish_each);
        }

        public View getView() {
            return view;
        }

        public ImageView getIv_order_dish_each() {
            return iv_order_dish_each;
        }

        public TextView getTv_order_dish_each() {
            return tv_order_dish_each;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
            }
        }
    }
}
