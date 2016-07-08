package com.example.administrator.babygrowth01.babyparadise.playing_game;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.clothesMall.ClothesJson;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/21.
 */
public class GamesListFragment extends Fragment  {

    private String NOTE="games";
    /** some views to show the content */
    private View game_holder;
    private RecyclerView games_list_recycler_view;
    private GamesListRecycleAdapter gamesListRecycleAdapter;
//    private PullToRefreshView pullToRefreshView;
    private LinearLayoutManager linearLayoutManager;

    /** ready to add more data */
    private RequestQueue requestQueue;

    /** async transmit content */
    private ArrayList<GamesJson> arraylist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        game_holder =inflater.inflate(R.layout.baby_game, container, false);
        MyResource.setFragment_show("GamesListFragment");
        return game_holder;
    }


    @Override
    public void onStart() {
        super.onStart();
        /** find the holder preparing for the refresh action */
//        pullToRefreshView= (PullToRefreshView) game_holder.findViewById(R.id.pull_to_refresh);
        arraylist=new ArrayList<>();
        init(arraylist);
        loadData();
    }

    public void init(ArrayList<GamesJson> arraylist){

        /** refresh for more registration */
//        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                /** ask for more data*/
//                loadData();
//                pullToRefreshView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        pullToRefreshView.setRefreshing(false);
//                    }
//                },1000);
//            }
//        });

        /** initiation about requesting for data */
        requestQueue=Volley.newRequestQueue(game_holder.getContext());

        /** do the preparation to use the recyclerview*/
        games_list_recycler_view = (RecyclerView) game_holder.findViewById(R.id.playing_game_recyclerview);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        games_list_recycler_view.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));
        /** set the adapter to load the content to the view */
        gamesListRecycleAdapter =new GamesListRecycleAdapter(arraylist,this.getActivity());
        games_list_recycler_view.setAdapter(gamesListRecycleAdapter);
        /** set the animation*/
        games_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        //games_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), games_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //games_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }


    public void addData(List<GamesJson> arrayList){
        this.arraylist.addAll(0, arrayList);
        gamesListRecycleAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(0);
    }


    /** when the user like to seek more data from the internet */
    public void loadData(){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Games/loadGamesInfo"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String thumbnail_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Games/thumbnail/";

                List<GamesJson> results= JSON.parseArray(s, GamesJson.class);

                /* add the base uri */
                for (GamesJson gamesJson:
                        results) {
                    gamesJson.setGame_thumbnail_uri(thumbnail_uri_base_root+gamesJson.getGame_thumbnail_uri());
                }
                MyResource.games_id+=10;
                addData(results);
                requestQueue.cancelAll("seek_more_games_data");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error post");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("id",String.valueOf(MyResource.games_id));
                return map;
            }
        };
        request_more.setTag("seek_more_games_data");
        requestQueue.add(request_more);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        MyResource.games_id=1;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
