package com.example.administrator.babygrowth01.Common.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.administrator.babygrowth01.babyparadise.parentMedia.ParentMediaJson;
import com.example.administrator.babygrowth01.babyparadise.songs.SongsJson;
import com.example.administrator.babygrowth01.R;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/23.
 */
public class CreateDatabse {


    /***********************************************************************************************/
    /** songs list */
    public void createTable(SQLiteDatabase db){

        /** create BabyPaFragment.songs list table */
        db.execSQL("CREATE TABLE songs_list(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "status INTEGER," +
                "play_times INTEGER," +
                "thumbnail_uri TEXT," +
                "http_uri TEXT," +
                "local_uri TEXT)");

        /** create ParentMedia.media list table */
        /*db.execSQL("CREATE TABLE parent_media(" +
                "child_id INTEGER," +
                "type INTEGER," +
                "age_layer INTEGER," +
                "item_id INTEGER)");*/

        /** create children_records table */
        db.execSQL("CREATE TABLE children_records(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "child_id INTEGER,"+
                "pic_uri TEXT," +
                "note TEXT," +
                "date_time TEXT," +
                "height INTEGER)");

        /** create children information record*/
        db.execSQL("CREATE TABLE children_info(" +
                "child_id INTEGER," +
                "portrait_uri TEXT," +
                "nick_name TEXT," +
                "gender TEXT," +
                "birth TEXT)");

        System.out.println("create new");
    }

    public void addSongs(SQLiteDatabase db,Context context){
        try {

            /** prepare the BabyPaFragment.songs information from json file */
            InputStream is=context.getResources().openRawResource(R.raw.songs);
            InputStreamReader isr = new InputStreamReader(is,"UTF-8");
            /*char [] input=new char[is.available()];
            isr.read(input);*/
            BufferedReader buf = new BufferedReader(isr);
            String read =""; String json_str="";
            while ((read=buf.readLine()) != null) {
                json_str+=read;
            } buf.close();isr.close(); is.close();

            List<SongsJson> results=JSON.parseArray(json_str, SongsJson.class);
            //System.out.println("result "+results);
            /**
             * insert into to the SQLite database
             */
            for (SongsJson songs_list :
                    results) {

                ContentValues cv=new ContentValues();
                cv.put("name", songs_list.getName());
                cv.put("status", songs_list.getStatus());
                cv.put("play_times", songs_list.getPlay_times());
                cv.put("thumbnail_uri", songs_list.getThumbnail_uri());
                cv.put("http_uri", songs_list.getHttp_uri());
                cv.put("local_uri", songs_list.getLocal_uri());
                db.insert("songs_list", null, cv);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*public void addParentMedia(SQLiteDatabase db,Context context){
        try {
            *//** prepare the BabyPaFragment.songs information from json file *//*
            InputStream is=context.getResources().openRawResource(R.raw.parent_media);
            InputStreamReader isr = new InputStreamReader(is,"UTF-8");
            BufferedReader buf = new BufferedReader(isr);
            String read =""; String json_str="";
            while ((read=buf.readLine()) != null) {
                json_str+=read;
            } buf.close();isr.close(); is.close();

            *//** initiate the gson tool ready for interpret json*//*
            List<ParentMediaJson> results=JSON.parseArray(json_str, ParentMediaJson.class);

            *//** insert into to the SQLite database*//*
            for (ParentMediaJson parent_media :
                    results) {

                ContentValues cv=new ContentValues();
                cv.put("id", parent_media.get_id());
                cv.put("thumbnail_uri", parent_media.getThumbnail_uri());
                cv.put("depict", parent_media.getDepict());
                cv.put("media_uri", parent_media.getMedia_uri());
                cv.put("type", parent_media.getType());
                cv.put("age_layer", parent_media.getAge_layer());
                db.insert("parent_media", null, cv);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
