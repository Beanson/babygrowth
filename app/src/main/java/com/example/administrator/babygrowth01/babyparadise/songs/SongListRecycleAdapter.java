package com.example.administrator.babygrowth01.babyparadise.songs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.Common.MediaPlayer.MediaPlayActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class SongListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<SongsJson> arrayList;
    private Context context;

    public SongListRecycleAdapter(ArrayList<SongsJson> arrayList,Context context) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.songs_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //System.out.println("on bind view holder");
        MyViewHolder vh = (MyViewHolder) holder;
        /** binding some addition message to a view*/ vh.getView().setTag(R.id.rv_tag_1,arrayList.get(position).getHttp_uri());
        /** binding some addition message to a view*/ vh.getView().setTag(R.id.rv_tag_2,"tag_message use later");
        /** binding some addition message to a view*/ vh.getView().setTag(R.id.rv_tag_2,"tag_message use later");
        /** BabyPaFragment.songs image*/ Picasso.with(vh.getSongs_image().getContext()).load(arrayList.get(position).getThumbnail_uri()).resize(70,50).into(vh.getSongs_image());
        /** BabyPaFragment.songs play icon*/ Picasso.with(vh.getPlay_songs().getContext()).load("file:///android_asset/common/play.png").into(vh.getPlay_songs());
        /** BabyPaFragment.songs download icon*/ //Picasso.with(vh.getDownload_songs().getContext()).load("file:///android_asset/common/download.png").into(vh.getDownload_songs());
        /** BabyPaFragment.songs delete icon*/ //Picasso.with(vh.getDelete_songs().getContext()).load("file:///android_asset/common/trash.png").into(vh.getDelete_songs());
        /** BabyPaFragment.songs name */ vh.getSongs_name().setText(arrayList.get(position).getName());
    }


    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView songs_image;
        private TextView songs_name;
        private ImageView play_songs,download_songs,delete_songs;
        private View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            songs_image= (ImageView) itemView.findViewById(R.id.songs_image);
            songs_name=(TextView)itemView.findViewById(R.id.songs_name);
            play_songs= (ImageView) itemView.findViewById(R.id.play_songs);
//            download_songs= (ImageView) itemView.findViewById(R.id.download_songs);
//            delete_songs= (ImageView) itemView.findViewById(R.id.delete_songs);

            songs_image.setOnClickListener(this);
            songs_name.setOnClickListener(this);
            play_songs.setOnClickListener(this);
//            download_songs.setOnClickListener(this);
//            delete_songs.setOnClickListener(this);
        }

        public ImageView getSongs_image() {
            return songs_image;
        }

        public TextView getSongs_name() {
            return songs_name;
        }

        public ImageView getPlay_songs() {
            return play_songs;
        }

//        public ImageView getDownload_songs() {
//            return download_songs;
//        }
//
//        public ImageView getDelete_songs() {
//            return delete_songs;
//        }

        public View getView(){return itemView;}
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.songs_image:{

                    break;
                }
                case R.id.songs_name:{

                    break;
                }
                case R.id.play_songs:{

                    break;
                }
                default:{
                    break;
                }
//                case R.id.download_songs:{
//
//                    break;
//                }
//                case R.id.delete_songs:{
//
//                    break;
//                }
            }
        }
    }

    /*public void playSongs(String url){
        Intent intent=new Intent(context, MediaPlayActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }*/

    public void refreshAddItem(int position,ArrayList<SongsJson> arrayListMore){
        System.out.println("do refresh");
        for (SongsJson songsMore :
                arrayListMore) {
            arrayList.add(0,songsMore);
            notifyItemInserted(position);
        }
    }
}
