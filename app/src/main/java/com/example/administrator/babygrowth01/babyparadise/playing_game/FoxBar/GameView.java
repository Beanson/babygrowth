package com.example.administrator.babygrowth01.babyparadise.playing_game.FoxBar;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/23.
 */
public class GameView extends View {

    private Paint paint=new Paint();
    private AssetManager assets=null;
    private int tableWidth,tableHeight;

    public GameView(Context context,int tableWidth,int tableHeight) {
        super(context);
        this.context=context;
        this.tableHeight=tableHeight;
        this.tableWidth=tableWidth;
        initScene();
    }

    public void initScene(){
        assets=context.getAssets();
        InputStream is = null;
        try {
            is = assets.open("BabyPaFragment/game01/lotus.png");
            lotus= BitmapFactory.decodeStream(is);
            is = assets.open("BabyPaFragment/game01/fox.png");
            fox= BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fox place initialize
        int fox_each=(tableHeight-50)/5;
        fox_place=new int[]{0,fox_each,fox_each*2,fox_each*3,fox_each*4};

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置去锯齿
        paint.setAntiAlias(true);
        for(int i=0;i<fox_place.length;i++){
            canvas.drawBitmap(lotus, 0, fox_place[i], paint); //initiate lotus
        }
        canvas.drawBitmap(fox, 10, fox_place[2], paint); //initiate fox
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.currentX=event.getX();
        this.currentY=event.getY();
        fox_operation();
        return true;
    }

    /**
     * judge whether operation for fox operation
     * @return
     */
    public void fox_operation(){
        if(currentX<100&&currentY>50){
            for(int i=0;i<5;i++){
                if(currentX<fox_place[i]){

                }
            }
        }
    }

    private Context context;
    private Bitmap fox,lotus;
    private int fox_place[];
    private float currentX,currentY;
}
