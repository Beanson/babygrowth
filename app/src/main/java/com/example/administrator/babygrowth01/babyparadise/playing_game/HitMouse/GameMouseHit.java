package com.example.administrator.babygrowth01.babyparadise.playing_game.HitMouse;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.songs.SongsJson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class GameMouseHit extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        init();
    }

    public void init(){
        //time_last= (TextView) findViewById(R.id.time_last);
        get_score= (TextView) findViewById(R.id.get_score);
        mouse_hit= (GridLayout) findViewById(R.id.mouse_hit);
        mouse_01= (ImageView) findViewById(R.id.mouse_01);
        mouse_02= (ImageView) findViewById(R.id.mouse_02);
        mouse_03= (ImageView) findViewById(R.id.mouse_03);
        mouse_04= (ImageView) findViewById(R.id.mouse_04);
        mouse_05= (ImageView) findViewById(R.id.mouse_05);
        mouse_06= (ImageView) findViewById(R.id.mouse_06);
        mouse_07= (ImageView) findViewById(R.id.mouse_07);
        mouse_08= (ImageView) findViewById(R.id.mouse_08);
        mouse_09= (ImageView) findViewById(R.id.mouse_09);
        begin_play= (Button) findViewById(R.id.begin_play);
        stop_play= (Button) findViewById(R.id.stop_play);

        begin_play.setOnClickListener(this);
        stop_play.setOnClickListener(this);
        mouse_01.setOnClickListener(this);
        mouse_02.setOnClickListener(this);
        mouse_03.setOnClickListener(this);
        mouse_04.setOnClickListener(this);
        mouse_05.setOnClickListener(this);
        mouse_06.setOnClickListener(this);
        mouse_07.setOnClickListener(this);
        mouse_08.setOnClickListener(this);
        mouse_09.setOnClickListener(this);

        client_Messenger=new Messenger(new HandlerActivity());
        service_Messenger= MyResource.getService_Messenger();

    }


    /**
     * handler for mouse continuing coming out
     */
    public class HandlerActivity extends Handler {
        @Override
        public void handleMessage(Message msgService) {
            super.handleMessage(msgService);

            switch (msgService.arg1){

                /** 91 receive mark the interval time finish ,another new mouse should come out */
                case 91:{
                    if(play_mark){
                        playInit();
                        client_message=new Message();
                        client_message.arg1=91;  //arg1=91 for beginning the game playing
                        sendToService(client_message);
                        client_message=null;  //release the resource
                    }
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }
    public void sendToService(Message msg){
        /** binding to the messenger and to the handler */
        msg.replyTo=client_Messenger;
        try {
            service_Messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * initiate the game widgets
     */
    public void playInit(){

        mouse_out_mun=createRamdom();
        if(anim!=null){
            anim.stop();
        }
        switch (mouse_out_mun){
            case 0:{
                mouse_01.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_01.getBackground();
                anim.start();
                break;
            }
            case 1:{
                mouse_02.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_02.getBackground();
                anim.start();
                break;
            }
            case 2:{
                mouse_03.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_03.getBackground();
                anim.start();
                break;
            }
            case 3:{
                mouse_04.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_04.getBackground();
                anim.start();
                break;
            }
            case 4:{
                mouse_05.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_05.getBackground();
                anim.start();
                break;
            }
            case 5:{
                mouse_06.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_06.getBackground();
                anim.start();
                break;
            }
            case 6:{
                mouse_07.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_07.getBackground();
                anim.start();
                break;
            }
            case 7:{
                mouse_08.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_08.getBackground();
                anim.start();
                break;
            }
            case 8:{
                mouse_09.setBackgroundResource(R.drawable.mouse_out);
                anim= (AnimationDrawable) mouse_09.getBackground();
                anim.start();
                break;
            }
        }
    }

    /**
     * create random num to choose mouse in random
     * @return
     */
    public int createRamdom(){
        return random.nextInt(8);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.begin_play:{
                playInit();
                play_mark=true;
                client_message=new Message();
                client_message.arg1=91;  //arg1=91 for beginning the game playing
                sendToService(client_message);
                client_message=null;  //release the resource
                break;
            }
            case R.id.stop_play:{
                play_mark=false;
                if(anim!=null){
                    anim.stop();
                }
                if(anim_hit!=null){
                    anim_hit.stop();
                }
                break;
            }
            case R.id.mouse_01:{
                //whether the frame animation has stop
                if(mouse_out_mun==0){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_01.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_01.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_02:{
                //whether the frame animation has stop
                if(mouse_out_mun==1){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_02.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_02.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_03:{
                //whether the frame animation has stop
                if(mouse_out_mun==2){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_03.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_03.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_04:{
                //whether the frame animation has stop
                if(mouse_out_mun==3){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_04.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_04.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_05:{
                //whether the frame animation has stop
                if(mouse_out_mun==4){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_05.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_05.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_06:{
                //whether the frame animation has stop
                if(mouse_out_mun==5){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_06.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_06.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_07:{
                //whether the frame animation has stop
                if(mouse_out_mun==6){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_07.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_07.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_08:{
                //whether the frame animation has stop
                if(mouse_out_mun==7){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_08.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_08.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            case R.id.mouse_09:{
                //whether the frame animation has stop
                if(mouse_out_mun==8){
                    if(anim!=null){
                        anim.stop();
                    }
                    if(anim_hit!=null){
                        anim_hit.stop();
                    }
                    mouse_09.setBackgroundResource(R.drawable.hit_mouse);
                    anim_hit= (AnimationDrawable) mouse_09.getBackground();
                    anim_hit.start();
                    get_score.setText("Score: "+(++mark));
                }
                break;
            }
            default:{
                break;
            }
        }
    }



    private TextView time_last,get_score;
    private GridLayout mouse_hit;
    private ImageView mouse_01,mouse_02,mouse_03,mouse_04,mouse_05,mouse_06,mouse_07,mouse_08,mouse_09;
    private AnimationDrawable anim,anim_hit;
    private Button begin_play,stop_play;
    private boolean play_mark=false;
    private int mouse_out_mun,mark=0;
    private Messenger client_Messenger;
    private Messenger service_Messenger;
    private Message client_message;
    private Random random=new Random(8);

}
