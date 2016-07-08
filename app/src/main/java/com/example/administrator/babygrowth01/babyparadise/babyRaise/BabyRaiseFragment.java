package com.example.administrator.babygrowth01.babyparadise.babyRaise;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.example.administrator.babygrowth01.babyparadise.fun_play.FunPlayJson;
import com.example.administrator.babygrowth01.babyparadise.fun_play.FunPlayListRecycleAdapter;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/15.
 */
public class BabyRaiseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity=this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyResource.setFragment_show("BabyRaiseFragment");
        viewHolder=inflater.inflate(R.layout.baby_raise_sum, container, false);
        return viewHolder;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        loadData();
        GetWeather getWeather=new GetWeather(activity);
        getWeather.requestWhetherInfo(iv_weather, iv_show_clothes, iv_show_food, tv_data, tv_weather);
    }


    public void init(){
        iv_weather= (ImageView) viewHolder.findViewById(R.id.iv_weather);
        iv_show_clothes= (ImageView) viewHolder.findViewById(R.id.iv_show_clothes);
        //iv_show_food= (ImageView) viewHolder.findViewById(R.id.iv_show_food);
        tv_data= (TextView) viewHolder.findViewById(R.id.tv_data);
        tv_weather= (TextView) viewHolder.findViewById(R.id.tv_weather);
        ly_show_view= (LinearLayout) viewHolder.findViewById(R.id.ly_show_view);
        RelativeLayout life_tip= (RelativeLayout) viewHolder.findViewById(R.id.life_tip);
        final ImageView iv_open= (ImageView) viewHolder.findViewById(R.id.iv_open);

        final Animation close= AnimationUtils.loadAnimation(activity,R.anim.raise_hide);// add animation
        close.setFillAfter(true);//set remain status after animation over
        final Animation open= AnimationUtils.loadAnimation(activity,R.anim.raise_show);// add animation
        open.setFillAfter(true);//set remain status after animation over

//        final Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                -1.0f);
//        mHiddenAction.setDuration(500);

        ly_show_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(opened){
                    //ly_show_view.startAnimation(close);
                    ly_show_view.setVisibility(View.GONE);
                    iv_open.setVisibility(View.VISIBLE);
                    opened=false;
                }
            }
        });

        life_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!opened){
                    //ly_show_view.startAnimation(open);
                    ly_show_view.setVisibility(View.VISIBLE);
                    iv_open.setVisibility(View.GONE);
                    opened=true;
                }
            }
        });



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
        requestQueue= Volley.newRequestQueue(viewHolder.getContext());
        /** do the preparation to use the recyclerview*/
        recyclerView = (RecyclerView) viewHolder.findViewById(R.id.expert_suggest);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        recyclerView.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));
        /** set the adapter to load the content to the view */
        exSuListRecycleAdapter =new ExSuListRecycleAdapter(arraylist,activity);
        recyclerView.setAdapter(exSuListRecycleAdapter);
        /** set the animation*/
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        //fun_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), fun_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //fun_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    public void addData(List<ExpertSuJson> arrayList){
        this.arraylist.addAll(0, arrayList);
        exSuListRecycleAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(0);
    }

    /** when the user like to seek more data from the internet */
    public void loadData(){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/ParentMedia/addMoreParentMedia"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String es_thumbnail_uri_base="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/ParentMedia/thumbnail/";
                List<ExpertSuJson> results= JSON.parseArray(s, ExpertSuJson.class);

                /* add the base uri */
                for (ExpertSuJson expertSuJson:
                        results) {
                    expertSuJson.setThumbnail_uri(es_thumbnail_uri_base + expertSuJson.getThumbnail_uri());
                }
                addData(results);
                requestQueue.cancelAll("seek_more_ex_su_data");
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
                return map;
            }
        };
        request_more.setTag("seek_more_ex_su_data");
        requestQueue.add(request_more);
    }



    private ImageView iv_weather,iv_show_clothes,iv_show_food,iv_show_living,iv_show_walking;
    private TextView tv_data;
    private TextView tv_weather;
    private Activity activity;
    private View viewHolder;
    private RecyclerView recyclerView;
    private ExSuListRecycleAdapter exSuListRecycleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RequestQueue requestQueue; //request queue that get data from internet
    private ArrayList<ExpertSuJson> arraylist=new ArrayList<>(); // arraylist to load data
    private LinearLayout ly_show_view;
    private boolean opened=true;
}
