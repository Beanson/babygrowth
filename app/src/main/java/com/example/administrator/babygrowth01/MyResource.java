package com.example.administrator.babygrowth01;

import android.app.Application;
import android.os.Messenger;

import com.example.administrator.babygrowth01.Common.SQLite.SQLite;
import com.example.administrator.babygrowth01.Common.Handler.AsyncHandler;
import com.example.administrator.babygrowth01.babyrecords.Main.BabyInfo;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/2/22.
 */
public class MyResource extends Application {

    private static SQLite sqLite;
    private static String fragment_show="";
    private static String fragment_show_2="";
    private static String fragment_show_3="";
    private static AsyncHandler asyncHandler;
    private static Messenger service_Messenger;

    /** baby record */
    private static String record_table="children_records";
    private static String children_info_table="children_info";

    /** some path of the internet resource*/
    private static String parent_media="parent_media";

    /** some marks for the async task whether to continue */
    public static boolean media_mark=false;
    public static boolean flip_mark=false;

    /** for 3d model*/
    public static boolean mark=false;
    public static boolean mark_pre=false;
    public static int start_x;
    public static int start_y;
    public static int current_x;
    public static int current_y;
    public static int model_id=1;

    /* for clothes*/
    public static int clothes_id=1;

    /* for games*/
    public static int games_id=1;

    /* for course*/
    public static int course_id=1;


    @Override
    public void onCreate() {
        super.onCreate();

        /** preparing for the file folder later use*/
        /* create baby info folder */
        File file=new File(this.getFilesDir().getPath()+ BabyInfo.BABY_INFO_FOLDER);
        if(!file.exists()){
            file.mkdirs();
        }
        /* create baby record folder*/
        File file2=new File(this.getFilesDir().getPath()+ BabyInfo.BABY_RECORD_FOLDER);
        if(!file.exists()){
            file2.mkdirs();
        }

        // create this folder for the placing file4
        File file3=new File(this.getFilesDir().getPath()+BabyInfo.USER_INFO_FOLDER);
        if(!file3.exists()){
            file3.mkdirs();
        }

        initDb();
        initHandler();
    }

    /******************************************************************************************/
    /** init handler and messenger*/
    public void initHandler(){
        asyncHandler=new AsyncHandler("baby_growth_async",this);
        service_Messenger=asyncHandler.getServiceMessenger();
    }

    public static AsyncHandler getAsyncHandler() {
        return asyncHandler;
    }

    public static Messenger getService_Messenger() {
        return service_Messenger;
    }



    /***************************************************************************************/
    /**  about the database initiation */
    public void initDb(){
        sqLite=new SQLite(this,"babyGrowth",1);
    }

    public static SQLite getSqLite() {
        return sqLite;
    }

    public static String getChildren_info_table() {
        return children_info_table;
    }

    /*****************************************************************************************/
    /** for the use of activity and fragment communication */
    public static String getFragment_show() {
        return fragment_show;
    }

    public static void setFragment_show(String fragment_show) {
        MyResource.fragment_show = fragment_show;
    }

    /** this is mainly for baby record use */
    public static void setFragment_show_2(String fragment_show_2) {
        MyResource.fragment_show_2 = fragment_show_2;
    }

    /** for dish using*/
    public static String getFragment_show_3() {
        return fragment_show_3;
    }

    public static void setFragment_show_3(String fragment_show_3) {
        MyResource.fragment_show_3 = fragment_show_3;
    }

    /********************************************************************************************/
    /** get parent media information*/
    public static String getParent_media() {
        return parent_media;
    }



    /**********************************************************************************************8*/
    /** baby record */
    public static String getRecord_table() {
        return record_table;
    }


    @Override
    protected void finalize() throws Throwable {
        MyResource.getAsyncHandler().terminate();
        super.finalize();
    }

}
