package com.example.administrator.babygrowth01.babyparadise.babyRaise;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.Common.MediaPlayer.MediaPlayActivity;
import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.fun_play.FunPlayJson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */

public class ExSuListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<ExpertSuJson> arrayList;
    private Activity activity;
    private String NOTE="Raise";

    public ExSuListRecycleAdapter(ArrayList<ExpertSuJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.expert_suggest_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        /*binding id and expert suggest website */
        vh.getView().setTag(R.id.expert_suggest_uri, arrayList.get(position).getMedia_uri());
        vh.getTv_expert_su().setText(arrayList.get(position).getDepict());
        Picasso.with(activity).load(arrayList.get(position).getThumbnail_uri()).transform(new CircleTransform()).into(vh.getIv_expert_su());
    }

    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView iv_expert_su;
        private TextView tv_expert_su;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            iv_expert_su= (ImageView) itemView.findViewById(R.id.iv_expert_su);
            tv_expert_su= (TextView) itemView.findViewById(R.id.tv_expert_su);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri=view.getTag(R.id.expert_suggest_uri).toString();
                    uri="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/ParentMedia/video/"+uri;
                    Intent intent=new Intent(activity, MediaPlayActivity.class);
                    intent.putExtra("uri",uri);
                    intent.putExtra("service_Messenger", MyResource.getService_Messenger());
                    activity.startActivity(intent);
                }
            });
        }
        public View getView() {
            return view;
        }

        public ImageView getIv_expert_su() {
            return iv_expert_su;
        }

        public TextView getTv_expert_su() {
            return tv_expert_su;
        }
    }

}
