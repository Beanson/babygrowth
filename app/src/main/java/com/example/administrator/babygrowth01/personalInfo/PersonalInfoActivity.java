package com.example.administrator.babygrowth01.personalInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyrecords.Main.PortraitJson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class PersonalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initWidget();
        initData();
        initBabyInfo();
    }


    @Override
    public void onClick(View v) {

    }

    public void initBabyInfo(){
        ptjson=new ArrayList<PortraitJson>();
        new LoadBabyAsync(ptjson,ly_babyInfo,this).execute();
    }

    /**
     * get data from sharedPreference and set to cus_account textView
     */
    public void initData(){
        SharedPreferences sharedPreferences=this.getSharedPreferences("cus_info", Context.MODE_PRIVATE);
        String cus_account=sharedPreferences.getString("cus_account",null);
        tv_account.setText("账号信息    " + cus_account);
        Picasso.with(this).load(R.drawable.demo).transform(new CircleTransform()).into(person_portrait);
    }

    /**
     * do some initiation before use those widgets
     */
    public void initWidget(){
        bg_header= (ImageView) findViewById(R.id.bg_header);
        person_portrait= (ImageView) findViewById(R.id.person_portrait);
        tv_account= (TextView) findViewById(R.id.tv_account);
        ly_babyInfo= (LinearLayout) findViewById(R.id.ly_babyInfo);

        bg_header.setOnClickListener(this);
        person_portrait.setOnClickListener(this);
    }

    private ImageView bg_header,person_portrait;
    private TextView tv_account;
    private LinearLayout ly_babyInfo;
    private ArrayList<PortraitJson> ptjson;
}
