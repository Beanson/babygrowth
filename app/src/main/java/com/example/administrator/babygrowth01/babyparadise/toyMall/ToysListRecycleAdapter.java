package com.example.administrator.babygrowth01.babyparadise.toyMall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.songs.SongsJson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class ToysListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<ToysJson> arrayList;
    private Activity activity;

    public ToysListRecycleAdapter(ArrayList<ToysJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.toy_each,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        /** binding the id as tag with the view */ vh.getView().setTag(R.id.rv_tag_1, arrayList.get(position).getId());
        Picasso.with(activity).load(arrayList.get(position).getThumbnail_uri()).into(vh.getToy_thumbnail());
        vh.getToy_depict().setText(arrayList.get(position).getDepict());
    }


    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private Button see_3d,toy_short_add_cart,toy_short_buy;
        private ImageView toy_thumbnail;
        private TextView toy_depict;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            toy_thumbnail= (ImageView) itemView.findViewById(R.id.toy_thumbnail);
            toy_depict= (TextView) itemView.findViewById(R.id.toy_depict);
            see_3d= (Button) itemView.findViewById(R.id.see_3d);
            toy_short_add_cart= (Button) itemView.findViewById(R.id.toy_short_add_cart);
            toy_short_buy= (Button) itemView.findViewById(R.id.toy_short_buy);

            view.setOnClickListener(this);
            see_3d.setOnClickListener(this);
            toy_short_add_cart.setOnClickListener(this);
            toy_short_buy.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public ImageView getToy_thumbnail() {
            return toy_thumbnail;
        }

        public TextView getToy_depict() {
            return toy_depict;
        }

        public Button getSee_3d() {
            return see_3d;
        }

        public View getView(){return view;}

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.see_3d:{
                    MyResource.model_id=Integer.parseInt(view.getTag(R.id.rv_tag_1).toString());
                    Intent intent=new Intent(activity,Obj3DView.class);
                    activity.startActivity(intent);
                    break;
                }
                case R.id.toy_short_add_cart:{

                    break;
                }
                case R.id.toy_short_buy:{

                    break;
                }
                default:{

                    break;
                }
            }
        }
    }
}
