package com.example.administrator.babygrowth01.Common.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.babygrowth01.babyparadise.songs.SongsJson;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/2/23.
 */
public class SQLite extends SQLiteOpenHelper {

    private Context context;

    public SQLite(Context context, String name, int version) {
        super(context,name,null, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            /** create BabyPaFragment.songs list table */
            CreateDatabse createDatabse=new CreateDatabse();
            createDatabse.createTable(db);
            createDatabse.addSongs(db,context);
            //createDatabse.addParentMedia(db,context);
            //db.setTransactionSuccessful();
        }finally {
            //db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
