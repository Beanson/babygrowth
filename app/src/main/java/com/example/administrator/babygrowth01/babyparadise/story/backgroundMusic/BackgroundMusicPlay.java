package com.example.administrator.babygrowth01.babyparadise.story.backgroundMusic;

import android.app.Application;
import android.media.MediaPlayer;
import com.example.administrator.babygrowth01.R;

/**
 * Created by Administrator on 2016/2/20.
 */
public class BackgroundMusicPlay {

    private MediaPlayer mediaPlayer;
    private Application application;

    public BackgroundMusicPlay(Application application){
        this.application=application;
        init();
    }

    public void init(){
        mediaPlayer=MediaPlayer.create(application, R.raw.background_music_01);
        mediaPlayer.start();
    }

    public void onStop(){
        mediaPlayer.stop();
    }

    public void onRelease(){
        if(mediaPlayer.isPlaying()){
            onStop();
        }
        mediaPlayer.release();
        mediaPlayer=null;
    }
}
