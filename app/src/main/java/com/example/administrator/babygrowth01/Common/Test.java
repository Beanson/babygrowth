package com.example.administrator.babygrowth01.Common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.babygrowth01.babyparadise.story.backgroundMusic.BackgroundMusicPlay;

public class Test extends AppCompatActivity {

    BackgroundMusicPlay backgroundMusicPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //backgroundMusicPlay =new BackgroundMusicPlay("lalaal",this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //backgroundMusicPlay.onRelease();
    }
}
