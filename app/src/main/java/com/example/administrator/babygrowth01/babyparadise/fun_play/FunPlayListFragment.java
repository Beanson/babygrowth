package com.example.administrator.babygrowth01.babyparadise.fun_play;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.administrator.babygrowth01.babyparadise.playing_game.GamesJson;
import com.example.administrator.babygrowth01.babyparadise.playing_game.GamesListRecycleAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/21.
 */
public class FunPlayListFragment extends Fragment  {

    private String NOTE="FunPlay";
    /** some views to show the content */
    private View fun_holder;
    private RecyclerView fun_list_recycler_view;
    private FunPlayListRecycleAdapter funListRecycleAdapter;
//    private PullToRefreshView pullToRefreshView;
    private LinearLayoutManager linearLayoutManager;

    /** ready to add more data */
    private RequestQueue requestQueue;

    /** async transmit content */
    private ArrayList<FunPlayJson> arraylist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fun_holder =inflater.inflate(R.layout.fun_play, container, false);
        MyResource.setFragment_show("FunPlayListFragment");
        return fun_holder;
    }


    @Override
    public void onStart() {
        super.onStart();
        /** find the holder preparing for the refresh action */
//        pullToRefreshView= (PullToRefreshView) fun_holder.findViewById(R.id.pull_to_refresh);
        arraylist=new ArrayList<>();
        init(arraylist);
        loadData();
    }

    public void init(ArrayList<FunPlayJson> arraylist){

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
        requestQueue=Volley.newRequestQueue(fun_holder.getContext());
        /** do the preparation to use the recyclerview*/
        fun_list_recycler_view = (RecyclerView) fun_holder.findViewById(R.id.fun_play);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        fun_list_recycler_view.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));
        /** set the adapter to load the content to the view */
        funListRecycleAdapter =new FunPlayListRecycleAdapter(arraylist,this.getActivity());
        fun_list_recycler_view.setAdapter(funListRecycleAdapter);
        /** set the animation*/
        fun_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        //fun_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), fun_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //fun_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    public void addData(List<FunPlayJson> arrayList){
        this.arraylist.addAll(0, arrayList);
        funListRecycleAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(0);
    }

    /** when the user like to seek more data from the internet */
    public void loadData(){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/FunPlay/loadFunPlayInfo"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String fun_thumbnail_uri="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/FunGame/";

                List<FunPlayJson> results= JSON.parseArray(s, FunPlayJson.class);

                /* add the base uri */
                for (FunPlayJson funPlayJson:
                        results) {
                    funPlayJson.setFun_thumbnail(fun_thumbnail_uri+funPlayJson.getFun_thumbnail());
                }
                addData(results);
                requestQueue.cancelAll("seek_more_funPlay_data");
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
                Date date=new Date();
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=format.format(date);
                map.put("time",time);
                return map;
            }
        };
        request_more.setTag("seek_more_funPlay_data");
        requestQueue.add(request_more);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
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
