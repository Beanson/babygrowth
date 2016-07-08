package com.example.administrator.babygrowth01.babyparadise.playing_game.FoxBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.babygrowth01.R;

public class PlayGameActivity01 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉窗口标题
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play_game);
        //获取窗口管理器
        WindowManager windowManager=getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        display.getMetrics(metrics);
        //获取屏幕高和宽
        tableWidth=metrics.widthPixels;
        tableHeight=metrics.heightPixels;
        //初始化各组件
        init();
    }

    public void init(){
//        lotus_num= (TextView) findViewById(R.id.lotus_num);
//        iv_add_fox= (ImageView) findViewById(R.id.iv_add_fox);
//        iv_upgrade= (ImageView) findViewById(R.id.iv_upgrade);
//        iv_apply_skill= (ImageView) findViewById(R.id.iv_apply_skill);
//        game_land = (FrameLayout) findViewById(R.id.fox_land);

        iv_add_fox.setOnClickListener(this);
        iv_upgrade.setOnClickListener(this);
        iv_upgrade.setOnClickListener(this);
        iv_apply_skill.setOnClickListener(this);

        GameView gameView=new GameView(this,tableWidth,tableHeight);
        game_land.addView(gameView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.iv_add_fox:{
//
//                break;
//            }
//            case R.id.iv_upgrade:{
//
//                break;
//            }
//            case R.id.iv_apply_skill:{
//
//                break;
//            }
//            default:{
//                break;
//            }
        }
    }

    private TextView lotus_num;
    private ImageView iv_add_fox,iv_upgrade,iv_apply_skill;
    private FrameLayout game_land;
    private RelativeLayout game_holder;
    private int tableWidth,tableHeight;
}
