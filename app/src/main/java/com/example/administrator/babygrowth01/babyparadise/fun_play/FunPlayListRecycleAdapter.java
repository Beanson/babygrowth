package com.example.administrator.babygrowth01.babyparadise.fun_play;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */

public class FunPlayListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<FunPlayJson> arrayList;
    private Activity activity;
    private String NOTE="FunPlay";

    public FunPlayListRecycleAdapter(ArrayList<FunPlayJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.fun_play_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        /*binding id and fun website to the fun publishing platform */
        vh.getView().setTag(R.id.fun_website, arrayList.get(position).getWebsite());
        vh.getView().setTag(R.id.fun_id, arrayList.get(position).getId());

        /*bind data to thumbnail, theme, place, time, depict, website */
        vh.getFun_theme().setText(arrayList.get(position).getTheme());
        String time_temp=arrayList.get(position).getTime_begin()+"至\n"+arrayList.get(position).getTime_end();
//        time_temp=time_temp.replaceAll("\\r\\n\\s", " ");
//        Log.i(NOTE,time_temp);
        vh.getFun_time().setText(time_temp);
        vh.getFun_place().setText(arrayList.get(position).getPlace());
        vh.getFun_depict().setText(arrayList.get(position).getDepict());
        Picasso.with(activity).load(arrayList.get(position).getFun_thumbnail()).into(vh.getFun_thumbnail());
    }

    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView fun_thumbnail;
        private TextView fun_theme,fun_time,fun_place,fun_depict;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            fun_thumbnail= (ImageView) itemView.findViewById(R.id.fun_thumbnail);
            fun_theme= (TextView) itemView.findViewById(R.id.fun_theme);
            fun_time= (TextView) itemView.findViewById(R.id.fun_time);
            fun_place= (TextView) itemView.findViewById(R.id.fun_place);
            fun_depict= (TextView) itemView.findViewById(R.id.fun_depict);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = view.getTag(R.id.fun_website).toString(); // web address
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                }
            });
        }

        public View getView() {
            return view;
        }

        public ImageView getFun_thumbnail() {
            return fun_thumbnail;
        }

        public TextView getFun_theme() {
            return fun_theme;
        }

        public TextView getFun_time() {
            return fun_time;
        }

        public TextView getFun_place() {
            return fun_place;
        }

        public TextView getFun_depict() {
            return fun_depict;
        }
    }

}
