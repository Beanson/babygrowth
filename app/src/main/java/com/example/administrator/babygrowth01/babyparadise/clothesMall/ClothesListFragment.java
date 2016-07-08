package com.example.administrator.babygrowth01.babyparadise.clothesMall;

import android.app.Fragment;
import android.content.res.Resources;
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
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/21.
 */
public class ClothesListFragment extends Fragment  {

    /** some views to show the content */
    private View clothes_holder;
    private RecyclerView clothes_list_recycler_view;
    private ClothesListRecycleAdapter clothesListRecycleAdapter;
    private PullToRefreshView pullToRefreshView;
    private LinearLayoutManager linearLayoutManager;

    /** ready to add more data */
    private RequestQueue requestQueue;

    /** async transmit content */
    private ArrayList<ClothesJson> arraylist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clothes_holder =inflater.inflate(R.layout.toys_holder, container, false);
        MyResource.setFragment_show("ClothesListFragment");
        return clothes_holder;
    }


    @Override
    public void onStart() {
        super.onStart();
        /** find the holder preparing for the refresh action */
        pullToRefreshView= (PullToRefreshView) clothes_holder.findViewById(R.id.pull_to_refresh);
        arraylist=new ArrayList<>();
        init(arraylist);
        loadData();
    }

    public void init(ArrayList<ClothesJson> arraylist){

        /** refresh for more registration */
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /** ask for more data*/
                loadData();
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                },1000);
            }
        });

        /** initiation about requesting for data */
        requestQueue=Volley.newRequestQueue(clothes_holder.getContext());

        /** do the preparation to use the recyclerview*/
        clothes_list_recycler_view = (RecyclerView) clothes_holder.findViewById(R.id.toys_list_recycler_view);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        clothes_list_recycler_view.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));
        /** set the adapter to load the content to the view */
        clothesListRecycleAdapter =new ClothesListRecycleAdapter(arraylist,this.getActivity());
        clothes_list_recycler_view.setAdapter(clothesListRecycleAdapter);
        /** set the animation*/
        clothes_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        //clothes_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), clothes_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //clothes_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }


    public void addData(List<ClothesJson> arrayList){
        this.arraylist.addAll(0, arrayList);
        clothesListRecycleAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(0);
    }


    /** when the user like to seek more data from the internet */
    public void loadData(){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Clothes/loadClothesInfo"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String thumbnail_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Clothes/";

                List<ClothesJson> results= JSON.parseArray(s, ClothesJson.class);

                /* add the base uri */
                for (ClothesJson clothesJson:
                        results) {
                    clothesJson.getClothes_01().setThumbnail_uri(thumbnail_uri_base_root + clothesJson.getClothes_01().getThumbnail_uri());
                    clothesJson.getClothes_02().setThumbnail_uri(thumbnail_uri_base_root + clothesJson.getClothes_02().getThumbnail_uri());
                }
                MyResource.clothes_id+=5;
                addData(results);
                requestQueue.cancelAll("seek_more_clothes_data");
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
                map.put("id",String.valueOf(MyResource.clothes_id));
                return map;
            }
        };
        request_more.setTag("seek_more_clothes_data");
        requestQueue.add(request_more);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        MyResource.clothes_id=1;
        super.onDestroy();
    }
}
