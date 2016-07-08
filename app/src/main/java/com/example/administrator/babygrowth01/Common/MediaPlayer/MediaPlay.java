package com.example.administrator.babygrowth01.Common.MediaPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/2/20.
 */
public class MediaPlay implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener{

    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private String url;
    private SeekBar seekBar;
    private TextView textView_time;
    private BeginSeekBar beginSeekBar;
    private double final_time=0,start_time=0;
    private long final_minue;
    private long final_second;

    public MediaPlay(String url, SurfaceHolder surfaceHolder,SeekBar seekBar,TextView textView_time,BeginSeekBar beginSeekBar){
        this.url=url;
        this.beginSeekBar=beginSeekBar;
        this.surfaceHolder=surfaceHolder;
        this.seekBar=seekBar;
        this.textView_time=textView_time;
        init();
    }

    public void init(){
        try {
            System.out.println("media_uri:"+url);
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        onStart();
        final_time=mediaPlayer.getDuration();
        seekBar.setMax((int)final_time);
        final_minue= TimeUnit.MILLISECONDS.toMinutes((long) final_time);
        final_second=TimeUnit.MILLISECONDS.toSeconds((long) final_time)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) final_time));

        textView_time.setText(String.format(
                        "%02d:%02d/%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) start_time),
                        TimeUnit.MILLISECONDS.toSeconds((long) start_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) start_time)),
                        final_minue,
                        final_second)
        );

        /** first start to play the music or songs then to update the progress bar */
        seekBar.setProgress((int)start_time);
        beginSeekBar.doBegin();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        onRelease();
        return false;
    }


    public void onStart(){
        mediaPlayer.start();
        beginSeekBar.doBegin();
    }

    public void onPause(){
        mediaPlayer.pause();
    }

    public void onStop(){
        mediaPlayer.stop();
    }

    public void onSeekTo(int msec){
        mediaPlayer.seekTo(msec);
    }

    public void onRelease(){
        mediaPlayer.release();
        mediaPlayer=null;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /** for updating seek bar progress */
    public boolean updateSeekBar(){

        try{
            start_time=mediaPlayer.getCurrentPosition();
            textView_time.setText(String.format(
                            "%02d:%02d/%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) start_time),
                            TimeUnit.MILLISECONDS.toSeconds((long) start_time) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) start_time)),
                            final_minue,
                            final_second)
            );
            seekBar.setProgress((int)start_time);
            if(mediaPlayer.isPlaying()){
                return true;
            }
            return false;
        }catch (Exception e){
            return true;
        }

    }

    /** */
    public interface BeginSeekBar{
         void doBegin();
    }
}
