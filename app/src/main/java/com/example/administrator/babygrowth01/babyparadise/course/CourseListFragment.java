package com.example.administrator.babygrowth01.babyparadise.course;

import android.app.Fragment;
import android.content.ContentUris;
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
import com.example.administrator.babygrowth01.babyparadise.clothesMall.ClothesJson;
import com.example.administrator.babygrowth01.babyparadise.clothesMall.ClothesListRecycleAdapter;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/21.
 */
public class CourseListFragment extends Fragment  {

    /** some views to show the content */
    private View course_holder;
    private RecyclerView course_list_recycler_view;
    private CourseListRecycleAdapter courseListRecycleAdapter;
    private PullToRefreshView pullToRefreshView;
    private LinearLayoutManager linearLayoutManager;

    /** ready to add more data */
    private RequestQueue requestQueue;

    /** async transmit content */
    private ArrayList<CourseJson> arraylist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        course_holder =inflater.inflate(R.layout.course_list_fragment, container, false);
        MyResource.setFragment_show("CourseListFragment");
        return course_holder;
    }


    @Override
    public void onStart() {
        super.onStart();
        /** find the holder preparing for the refresh action */
        pullToRefreshView= (PullToRefreshView) course_holder.findViewById(R.id.pull_to_refresh);
        arraylist=new ArrayList<>();
        init(arraylist);
        loadData();
    }

    public void init(ArrayList<CourseJson> arraylist){

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
        requestQueue=Volley.newRequestQueue(course_holder.getContext());

        /** do the preparation to use the recyclerview*/
        course_list_recycler_view = (RecyclerView) course_holder.findViewById(R.id.course_list_recycler_view);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        course_list_recycler_view.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));
        /** set the adapter to load the content to the view */
        courseListRecycleAdapter =new CourseListRecycleAdapter(arraylist,this.getActivity());
        course_list_recycler_view.setAdapter(courseListRecycleAdapter);
        /** set the animation*/
        course_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        //course_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), course_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //course_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }


    public void addData(List<CourseJson> arrayList){
        this.arraylist.addAll(0, arrayList);
        courseListRecycleAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(0);
    }


    /** when the user like to seek more data from the internet */
    public void loadData(){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Course/loadCourseInfo"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Course/";
                //String video_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Clothes/";

                List<CourseJson> results= JSON.parseArray(s, CourseJson.class);

                /* add the base uri */
                for (CourseJson courseJson:
                        results) {
                    courseJson.setThumbnail_uri(base_root+courseJson.getThumbnail_uri());
                    courseJson.setVideo_uri(base_root+courseJson.getVideo_uri());
                }
                MyResource.course_id+=4;
                addData(results);
                requestQueue.cancelAll("seek_more_course_data");
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
                map.put("id",String.valueOf(MyResource.course_id));
                return map;
            }
        };
        request_more.setTag("seek_more_course_data");
        requestQueue.add(request_more);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        MyResource.course_id=1;
        super.onDestroy();
    }
}
