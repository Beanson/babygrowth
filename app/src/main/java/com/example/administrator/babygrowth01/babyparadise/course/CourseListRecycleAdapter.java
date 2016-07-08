package com.example.administrator.babygrowth01.babyparadise.course;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.Common.MediaPlayer.MediaPlayActivity;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class CourseListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<CourseJson> arrayList;
    private Activity activity;

    public CourseListRecycleAdapter(ArrayList<CourseJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.course_list_each,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder vh = (MyViewHolder) holder;

        Picasso.with(activity).load(arrayList.get(position).getThumbnail_uri()).into(vh.getIv_course());
        vh.getTv_course_name().setText(arrayList.get(position).getName());
        vh.getTv_course_learn().setText(arrayList.get(position).getLearn_content());

        vh.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyResource.course_id=1;
                /* to start the course video*/
                Intent intent = new Intent(activity, MediaPlayActivity.class);
                intent.putExtra("uri", arrayList.get(position).getVideo_uri());
                intent.putExtra("service_Messenger", MyResource.getService_Messenger());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView iv_course;
        private TextView tv_course_name,tv_course_learn;

        public MyViewHolder(View itemView) {
            super(itemView);
            /* do some initiation*/
            view = itemView;
            iv_course= (ImageView) itemView.findViewById(R.id.iv_course);
            tv_course_name= (TextView) itemView.findViewById(R.id.tv_course_name);
            tv_course_learn= (TextView) itemView.findViewById(R.id.tv_course_learn);
        }

        public View getView() {
            return view;
        }

        public ImageView getIv_course() {
            return iv_course;
        }

        public TextView getTv_course_name() {
            return tv_course_name;
        }

        public TextView getTv_course_learn() {
            return tv_course_learn;
        }
    }

}
