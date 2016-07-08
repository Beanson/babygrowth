package com.example.administrator.babygrowth01.Common.MediaPlayer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

public class MediaPlayActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private MediaPlay mediaPlay;
    private ImageView imageView;
    private RelativeLayout media_control,media_show_all;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private SeekBar seekBar;
    private TextView textView_time;
    private String uri;
    private Messenger client_Messenger;
    private Messenger service_Messenger;
    private Message client_message;
    private long exitTime=0;


    private boolean mark_media=false,mark_play=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_play_activity);

        /** receive uri data from the previous activity */
        Intent intent=getIntent();
        uri= (String) intent.getExtras().get("uri");
        service_Messenger=intent.getParcelableExtra("service_Messenger");

        /** do the init work*/
        media_control= (RelativeLayout) findViewById(R.id.media_control);
        media_show_all= (RelativeLayout) findViewById(R.id.media_show_all);
        imageView= (ImageView) findViewById(R.id.play_stop);
        seekBar= (SeekBar) findViewById(R.id.seek_bar);
        surfaceView= (SurfaceView) findViewById(R.id.surface);
        textView_time= (TextView) findViewById(R.id.textView_time);

        /** first add pause button image to the control view and set hidden */
        Picasso.with(this).load("file:///android_asset/common/media_play/pause.png").resize(40,40).into(imageView);
        media_control.setVisibility(View.GONE);

        /** get the surface holder to carry on the media */
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        /** when click this whether to show the control view or to collapse it*/
        media_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mark_media) {
                    media_control.setVisibility(View.GONE);
                    mark_media = false;
                } else {
                    media_control.setVisibility(View.VISIBLE);
                    mark_media = true;
                }
            }
        });

        /** to achieve the play and pause function */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mark_play){
                    mediaPlay.onPause();
                    Picasso.with(MediaPlayActivity.this).load("file:///android_asset/common/media_play/play.png").resize(40, 40).into(imageView);
                    mark_play=false;
                }else {
                    mediaPlay.onStart();
                    Picasso.with(MediaPlayActivity.this).load("file:///android_asset/common/media_play/pause.png").resize(40, 40).into(imageView);
                    mark_play=true;
                }
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlay=new MediaPlay(uri, surfaceHolder, seekBar, textView_time, new MediaPlay.BeginSeekBar() {
            @Override
            public void doBegin() {
                seekBarProHandler();
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        try{
            if(mediaPlay.getMediaPlayer().isPlaying()){
                mediaPlay.getMediaPlayer().stop();
            }
            mediaPlay.onRelease();
        }catch (Exception e){

        }finally {
            super.onDestroy();
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            /** to judge whether double click back-up key */
            if(System.currentTimeMillis()-exitTime>2000){
                Toast.makeText(this, "请再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
                return true;
            }else{
                MyResource.media_mark=false;
                try{
                    if(mediaPlay.getMediaPlayer().isPlaying()){
                        mediaPlay.getMediaPlayer().stop();
                        // remember never return false, as it couldn't function
                    }
                }catch (Exception e){

                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void seekBarProHandler(){

        MyResource.media_mark=true;
        client_Messenger=new Messenger(new HandlerActivity());
        client_message=new Message();
        client_message.arg1=11;  //arg1=11 for waiting for 1 second to update the progress bar
        sendToService(client_message);
        client_message=null;  //release the resource
    }

    public class HandlerActivity extends Handler {
        @Override
        public void handleMessage(Message msgService) {
            super.handleMessage(msgService);

            switch (msgService.arg1){

                /** 1 means has finished downloading the data and should do the initialization */
                case 11:{
                    if(mediaPlay!=null){
                        if(mediaPlay.updateSeekBar()){
                            seekBarProHandler();
                        }
                    }else{
                        seekBarProHandler();
                    }
                    //System.out.println("In client Receive 3. ID=" + Thread.currentThread().getId() + "  Name=" + Thread.currentThread().getName());
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
}
