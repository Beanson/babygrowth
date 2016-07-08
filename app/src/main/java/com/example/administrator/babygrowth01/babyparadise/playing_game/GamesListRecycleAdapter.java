package com.example.administrator.babygrowth01.babyparadise.playing_game;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class GamesListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<GamesJson> arrayList;
    private Activity activity;

    public GamesListRecycleAdapter(ArrayList<GamesJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.games_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        /*binding id and game uri to the game*/
        vh.getView().setTag(R.id.game_uri,arrayList.get(position).getGame_uri());
        vh.getView().setTag(R.id.game_id, arrayList.get(position).getId());

        /*bind data to game_thumbnail,game_name and game_depict*/
        Picasso.with(activity).load(arrayList.get(position).getGame_thumbnail_uri()).into(vh.getGames_thumb());
        vh.getGames_name().setText(arrayList.get(position).getGame_name());
        vh.getGames_depict().setText(arrayList.get(position).getGame_depict());
    }


    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView games_thumb;
        private TextView games_name, games_depict;


        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            games_depict = (TextView) itemView.findViewById(R.id.games_depict);
            games_name = (TextView) itemView.findViewById(R.id.games_name);
            games_thumb = (ImageView) itemView.findViewById(R.id.games_thumb);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = view.getTag(R.id.game_uri).toString(); // web address
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                }
            });
        }

        public View getView() {
            return view;
        }

        public ImageView getGames_thumb() {
            return games_thumb;
        }

        public TextView getGames_name() {
            return games_name;
        }

        public TextView getGames_depict() {
            return games_depict;
        }

    }

}
