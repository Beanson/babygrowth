package com.example.administrator.babygrowth01.babyparadise.baby_health;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
public class HealthFragment extends Fragment  {

    private View fun_holder;
    private WebView health_web;
    private TextView hospital_location;
    private RelativeLayout hos_location;
    private BabyHealthActivity activity;
    private int city_code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fun_holder =inflater.inflate(R.layout.health_care, container, false);
        MyResource.setFragment_show("HealthCareFragment");
        return fun_holder;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

    }

    public void init(){
        activity= (BabyHealthActivity) getActivity();
        hospital_location= (TextView) fun_holder.findViewById(R.id.hospital_location);
        hos_location= (RelativeLayout) fun_holder.findViewById(R.id.hos_location);
        health_web= (WebView) fun_holder.findViewById(R.id.health_web);
        /*initiate the sharedPreferences*/
        SharedPreferences preferences=activity.getSharedPreferences("cus_info", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=preferences.edit();
        city_code=preferences.getInt("city_code",0);// get city code from preference


        //initial the hasmap about heal_website and the index
        final HashMap<Integer,String> health_map=new HashMap<Integer,String>();
        health_map.put(0,"http://www.bjguahao.gov.cn/index.htm");
        health_map.put(1,"http://yuyue.shdc.org.cn/Main/Default.aspx");
        health_map.put(2,"http://www.nj12320.org/njres/");
        health_map.put(3,"http://gz.91160.com/search/index/cid-2918.html");
        health_map.put(4,"http://sz.91160.com/");

        //initial the hasmap about name and the index
        final HashMap<Integer,String> health_name=new HashMap<Integer,String>();
        health_name.put(0,"北京");
        health_name.put(1,"上海");
        health_name.put(2,"南京");
        health_name.put(3,"广州");
        health_name.put(4,"深圳");


        hospital_location.setText("所在地：" + health_name.get(city_code));
        health_web.loadUrl(health_map.get(city_code));
        /* click to switch city_code */
        hos_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{"北京", "上海", "南京", "广州", "深圳"}, city_code,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        city_code=which;
                                        System.out.println("mark test" + which);
                                    }
                                }
                        )
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putInt("city_code", city_code);
                                editor.apply();
                                hospital_location.setText("所在地：" + health_name.get(city_code));
                                health_web.loadUrl(health_map.get(city_code));
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
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

}
