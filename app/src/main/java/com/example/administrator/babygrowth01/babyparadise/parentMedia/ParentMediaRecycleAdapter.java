package com.example.administrator.babygrowth01.babyparadise.parentMedia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.songs.SongsJson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class ParentMediaRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<ParentMediaJson> arrayList;
    private Context context;

    public ParentMediaRecycleAdapter(ArrayList<ParentMediaJson> arrayList, Context context) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.parent_media_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //System.out.println("on bind view holder");
        MyViewHolder vh = (MyViewHolder) holder;
        /** binding media uri message to a view*/ vh.getView().setTag(R.id.rv_tag_1, arrayList.get(position).getMedia_uri());
        /** binding some addition message to a view*/ vh.getView().setTag(R.id.rv_tag_2, "tag_message use later");
        /** binding some addition message to a view*/ vh.getView().setTag(R.id.rv_tag_2, "tag_message use later");
        /** ParentMedia image*/ Picasso.with(vh.getImageView().getContext()).load(arrayList.get(position).getThumbnail_uri()).resize(50, 50).into(vh.getImageView());
        /** ParentMedia depict */ vh.getTextView().setText(arrayList.get(position).getDepict());
    }


    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView= (ImageView) itemView.findViewById(R.id.parent_image_thumbnail);
            textView= (TextView) itemView.findViewById(R.id.parent_image_depict);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }

    public void refreshAddItem(int position,ArrayList<ParentMediaJson> arrayListMore){
        System.out.println("do refresh");
        for (ParentMediaJson parentMediaJson :
                arrayListMore) {
            arrayList.add(0,parentMediaJson);
            notifyItemInserted(position);
        }
    }
}
