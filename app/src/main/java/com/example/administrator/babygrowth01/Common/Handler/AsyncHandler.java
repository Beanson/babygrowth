package com.example.administrator.babygrowth01.Common.Handler;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.babyparadise.parentMedia.ParentMediaJson;
import com.example.administrator.babygrowth01.babyparadise.songs.SongsJson;
import com.example.administrator.babygrowth01.babyparadise.story.backgroundMusic.BackgroundMusicPlay;
import com.example.administrator.babygrowth01.babyrecords.Main.PortraitJson;
import com.example.administrator.babygrowth01.babyrecords.Main.RecordJson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/2/22.
 */
public class AsyncHandler {

    private HandlerTask handlerTask=null;
    private String name=null;
    private Messenger service_Messenger;
    private Messenger client_Messenger;
    private HandlerThread handlerThread;
    private ArrayList<SongsJson> arrayList;
    private Message msgToClient;
    private Application application;

    /** about the database class*/
    private SQLiteDatabase dbWrite;

    /** background music */
    private BackgroundMusicPlay backgroundMusicPlay;

    public AsyncHandler(String name,Application application) {
        this.name=name;
        this.application=application;
        init();
    }

    public Messenger getServiceMessenger(){
        return service_Messenger;
    }

    class HandlerTask extends Handler {

        /** default constructor*/
        public HandlerTask(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msgFromClient) {
            super.handleMessage(msgFromClient);

            /** arg1 always put the int variable as the task number */
            switch (msgFromClient.arg1){

                /** task one is to load BabyPaFragment.songs data from the db and send back*/
                case 1:{
                    /** get the reference and add the data to the referred instance */
                    arrayList= (ArrayList<SongsJson>) msgFromClient.obj;
                    /** do the load BabyPaFragment.songs work within this method */
                    loadSongs(arrayList);
                    /** preparing the message to be sent */
                    msgToClient=new Message();
                    msgToClient.arg1=1;msgToClient.obj=arrayList;
                    /** send the client message */
                    sendToClient(msgFromClient,msgToClient);
                    /** release the resource*/
                    msgToClient=null;
                    break;
                }

                /** task eleven is to sleep a second and return back to update the progress bar*/
                case 11:{
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msgToClient=new Message();
                    msgToClient.arg1=11;
                    if(MyResource.media_mark){
                        sendToClient(msgFromClient,msgToClient);
                    }
                    msgToClient=null;
                    break;
                }

                /** task two is to loading parent media data from the db and send back */
                case 2:{
                    /*parent_arrayList= (ArrayList<ParentMediaJson>) msgFromClient.obj;
                    Bundle bundle=msgFromClient.getData();
                    int child_id=bundle.getInt("child_id");
                    int type=bundle.getInt("type");
                    String times=bundle.getString("times");
                    loadParentMediaList(parent_arrayList,child_id,type,times);
                    msgToClient=new Message();
                    msgToClient.arg1=2;msgToClient.obj=parent_arrayList;
                    sendToClient(msgFromClient,msgToClient);
                    msgToClient=null;*/

                    break;
                }

                case 22:{
                    break;
                }

                case 3:{
                    backgroundMusicPlay=new BackgroundMusicPlay(application);
                    loadChildrenRecord((ArrayList<RecordJson>) msgFromClient.obj);
                    msgToClient=new Message();
                    msgToClient.arg1=3;
                    sendToClient(msgFromClient,msgToClient);
                    msgToClient=null;
                    break;
                }
                case 33:{
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msgToClient=new Message();
                    msgToClient.arg1=33;
                    if(MyResource.flip_mark){
                        sendToClient(msgFromClient,msgToClient);
                    }
                    msgToClient=null;
                    break;
                }
                case 333:{
                    backgroundMusicPlay.onRelease();
                    break;
                }
                /* 5 for loading portrait data*/
                case 5:{

                    ArrayList<PortraitJson> portraitJsonArrayList= (ArrayList<PortraitJson>) msgFromClient.obj;
                    loadPortraitData(portraitJsonArrayList);
                    msgToClient=new Message();
                    msgToClient.arg1=5;
                    sendToClient(msgFromClient,msgToClient);
                    msgToClient=null;
                    break;
                }
                case 55:{
                    ArrayList<RecordJson> recordJsonArrayList= (ArrayList<RecordJson>) msgFromClient.obj;
                    loadRecordData(recordJsonArrayList,msgFromClient.arg2);
                    msgToClient=new Message();
                    msgToClient.arg1=55;
                    sendToClient(msgFromClient,msgToClient);
                    msgToClient=null;
                    break;
                }
                case 91:{
                    try {
                        TimeUnit.MILLISECONDS.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msgToClient=new Message();
                    msgToClient.arg1=91;
                    sendToClient(msgFromClient,msgToClient);
                    msgToClient=null;
                }
                default:{
                    break;
                }
            }
        }
    }

    public void terminate(){
        //handlerThread.quitSafely();  require api 18 or higher
        handlerThread.quit();
        handlerTask.removeCallbacks(handlerThread);
        System.out.println("terminate!");
    }

    public void init(){
        handlerThread=new HandlerThread(name);
        handlerThread.start();
        handlerTask=new HandlerTask(handlerThread.getLooper());
        service_Messenger = new Messenger(handlerTask);
        System.out.println("begin work done!");
    }


    public void loadSongs(ArrayList<SongsJson> arrayList){

        /** do some initiation */
        SQLiteDatabase dbReader=MyResource.getSqLite().getReadableDatabase();

        /** query the SQLite database */
        Cursor c=dbReader.query("songs_list",null,null,null,null,null,null);
        while (c.moveToNext()){
            SongsJson songsJson=new SongsJson();
            songsJson.setName(c.getString(c.getColumnIndex("name")));
            songsJson.setStatus(c.getInt(c.getColumnIndex("status")));
            songsJson.setPlay_times(c.getInt(c.getColumnIndex("play_times")));
            songsJson.setThumbnail_uri(c.getString(c.getColumnIndex("thumbnail_uri")));
            songsJson.setHttp_uri(c.getString(c.getColumnIndex("http_uri")));
            songsJson.setLocal_uri(c.getString(c.getColumnIndex("local_uri")));
            arrayList.add(songsJson);
        }
        /** release the resource */
        c.close();dbReader.close();
    }

    /** for loading child record and show as a calendar telling stories */
    public void loadChildrenRecord(ArrayList<RecordJson> arrayList){
        /** do some initiation */
        SQLiteDatabase dbReader=MyResource.getSqLite().getReadableDatabase();

        /** query the SQLite database */
        Cursor c=dbReader.query("children_records",new String[]{"id","child_id","pic_uri","date_time","note"},null,null,null,null,null);
        while (c.moveToNext()){
            RecordJson recordJson=new RecordJson();
            recordJson.setId(c.getInt(c.getColumnIndex("id")));
            recordJson.setChild_id(c.getInt(c.getColumnIndex("child_id")));
            recordJson.setPic_uri(c.getString(c.getColumnIndex("pic_uri")));
            recordJson.setDate_time(c.getString(c.getColumnIndex("date_time")));
            recordJson.setNote(c.getString(c.getColumnIndex("note")));
            arrayList.add(recordJson);
        }
        /** release the resource */
        c.close();dbReader.close();
    }


    /** loading parent media */
    public void loadParentMediaList(ArrayList<ParentMediaJson> arrayList,int child_id,int type,String times){
        int age_layer,id;

    /** first to init the age_layer and id preparing for the searching data*/
        if(child_id!=0){ //means there exist kids
            /** searching for age_layer and id */
            SQLiteDatabase dbReader=MyResource.getSqLite().getReadableDatabase();
            Cursor c=dbReader.query(MyResource.getParent_media(),null,"child_id="+child_id+" and type="+type,null,null,null,null);
            c.moveToFirst();
            age_layer=c.getInt(c.getColumnIndex("age_layer"));
            id=c.getInt(c.getColumnIndex("id"));
            /** release the resource */
            c.close();dbReader.close();
        }else{
            /** haven't add any chil   d */
            age_layer=1;id=1;
        }

    /** use the volley tool to ask request to the internet back server to load the data*/
        //MyResource.getVolleyTool().getMediaParentJSONVolley(arrayList,type,age_layer,id,times);

    /** update the database after loading the data */
        SQLiteDatabase dbWriter=MyResource.getSqLite().getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",id+10);
        dbWriter.update(MyResource.getParent_media(), values, "child_id=" + child_id + " and type=" + type, null);
        dbWriter.close();
    }

    /** loading child portrait data*/
    public void loadPortraitData(ArrayList<PortraitJson> portraitJsonArrayList){
        SQLiteDatabase dbReader=MyResource.getSqLite().getReadableDatabase();
        Cursor cursor=dbReader.query(MyResource.getChildren_info_table(),null,null,null,null,null,null);
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                PortraitJson portraitJson=new PortraitJson();
                portraitJson.setChild_id(cursor.getInt(cursor.getColumnIndex("child_id")));
                portraitJson.setPortrait_uri(cursor.getString(cursor.getColumnIndex("portrait_uri")));
                portraitJson.setNick_name(cursor.getString(cursor.getColumnIndex("nick_name")));
                portraitJson.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                portraitJson.setBirth(cursor.getString(cursor.getColumnIndex("birth")));
                portraitJsonArrayList.add(0,portraitJson);
            }while (cursor.moveToNext());
            cursor.close();
        }
        dbReader.close();

    }

    /** for loading child record data*/
    public void loadRecordData(ArrayList<RecordJson> recordJsonArrayList,int child_id){
        /** get database reference from application */
        SQLiteDatabase dbReader=MyResource.getSqLite().getReadableDatabase();
        recordJsonArrayList.clear();// clear the previous data and load new data
        /** query the SQLite database */
        Cursor c=dbReader.query(MyResource.getRecord_table(),null,"child_id="+child_id,null,null,null,"id desc");

        if(c.getCount()>0){
            c.moveToFirst();
            do{
                RecordJson recordJson=new RecordJson();
                recordJson.setId(c.getInt(c.getColumnIndex("id")));
                recordJson.setChild_id(c.getInt(c.getColumnIndex("child_id")));
                recordJson.setPic_uri(c.getString(c.getColumnIndex("pic_uri")));
                recordJson.setNote(c.getString(c.getColumnIndex("note")));
                recordJson.setDate_time(c.getString(c.getColumnIndex("date_time")));
                recordJson.setHeight(c.getInt(c.getColumnIndex("height")));
                recordJsonArrayList.add(recordJson);
            }while (c.moveToNext());
        }
        /** release the resource */
        c.close();dbReader.close();
    }

    /** response to the request */
    public void sendToClient(Message msgFromClient,Message msgToClient){
        client_Messenger=msgFromClient.replyTo;
        msgToClient.replyTo=service_Messenger;
        System.out.println("In Service ID="+Thread.currentThread().getId()+"  Name="+Thread.currentThread().getName());
        try {
            client_Messenger.send(msgToClient);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
