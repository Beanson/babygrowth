package com.example.administrator.babygrowth01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.FirstTime.UserInfoFragment;
import com.example.administrator.babygrowth01.babyparadise.BabyPaFragment;
import com.example.administrator.babygrowth01.babyrecords.Main.Child_Time_Record_Activity;
import com.example.administrator.babygrowth01.babyrecords.Main.PortraitJson;
import com.example.administrator.babygrowth01.familydish.DishListActivity;
import com.example.administrator.babygrowth01.personalInfo.LoadBabyAsync;
import com.example.administrator.babygrowth01.personalInfo.PersonalInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,DrawerLayout.DrawerListener{

    LinearLayout ly_baby_paradise, lv_baby_record,ly_baby_food;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // 设置进入Activity的Activity特效动画，同理可拓展为布局动画
        /*SwitchLayout.getSlideFromBottom(this, false,
                BaseEffects.getQuickToSlowEffect());*/
        // 三个参数分别为（Activity/View，是否关闭Activity，特效（可为空））

        init();
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.MainFragment, new BabyPaFragment())
                .commit();

    }

    public void init(){
        ly_baby_paradise= (LinearLayout) findViewById(R.id.ly_baby_paradise);
        lv_baby_record= (LinearLayout) findViewById(R.id.lv_baby_record);
        ly_baby_food= (LinearLayout) findViewById(R.id.ly_baby_food);

        ly_baby_paradise.setOnClickListener(this);
        lv_baby_record.setOnClickListener(this);
        ly_baby_food.setOnClickListener(this);

        //about the left slide initiation
        initWidget();
        initData();
        initBabyInfo();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        String fragment_show=MyResource.getFragment_show();

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            switch (fragment_show){
                case "SongsListFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show("BabyPaFragment");
                    break;
                }
                case "BabyRaiseFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show("BabyPaFragment");
                    break;
                }
                case "ToysListFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show("BabyPaFragment");
                    break;
                }
                case "ClothesListFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show("BabyPaFragment");
                    break;
                }
                case "CourseListFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show("BabyPaFragment");
                    break;
                }
                case "GamesListFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show("BabyPaFragment");
                    break;
                }
                case "FunPlayListFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show("BabyPaFragment");
                    break;
                }
                case "BabyPaFragment":{
                    return super.onKeyDown(keyCode, event);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        String fragment_show=MyResource.getFragment_show();

        switch (v.getId()){
            case R.id.ly_baby_paradise:{


                /** it is interesting with the add and pop fragment mechanism*/
                switch (fragment_show){
                    case "SongsListFragment":{
                        getFragmentManager().popBackStack();
                        MyResource.setFragment_show("BabyPaFragment");
                        break;
                    }
                    case "BabyRaiseFragment":{
                        getFragmentManager().popBackStack();
                        MyResource.setFragment_show("BabyPaFragment");
                        break;
                    }
                    case "ToysListFragment":{
                        getFragmentManager().popBackStack();
                        MyResource.setFragment_show("BabyPaFragment");
                        break;
                    }
                    case "ClothesListFragment":{
                        getFragmentManager().popBackStack();
                        MyResource.setFragment_show("BabyPaFragment");
                        break;
                    }
                    case "CourseListFragment":{
                        getFragmentManager().popBackStack();
                        MyResource.setFragment_show("BabyPaFragment");
                        break;
                    }
                    case "GamesListFragment":{
                        getFragmentManager().popBackStack();
                        MyResource.setFragment_show("BabyPaFragment");
                        break;
                    }
                    case "FunPlayListFragment":{
                        getFragmentManager().popBackStack();
                        MyResource.setFragment_show("BabyPaFragment");
                        break;
                    }
                    case "BabyPaFragment":{
                        break;
                    }
                    default:{
                        break;
                    }
                }
                break;
            }
            case R.id.lv_baby_record:{
                Intent intent=new Intent(MainActivity.this, Child_Time_Record_Activity.class);
                startActivity(intent);
                break;
            }
            case R.id.ly_baby_food:{
                Intent intent=new Intent(MainActivity.this, DishListActivity.class);
                startActivity(intent);
//                Intent intent=new Intent(MainActivity.this, PersonalInfoActivity.class);
//                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

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
