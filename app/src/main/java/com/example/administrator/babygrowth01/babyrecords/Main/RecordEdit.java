package com.example.administrator.babygrowth01.babyrecords.Main;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.FirstTime.Customer;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.toyMall.ToysJson;
import com.example.administrator.babygrowth01.babyrecords.AliOss.OssClass;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/31.
 */
public class RecordEdit {

    public RecordEdit(Child_Time_Record_Activity activity,String pic_uri,int child_id,Context applicationContext) {
        this.activity = activity;
        this.pic_uri=pic_uri;
        this.child_id=child_id;
        this.applicationContext=applicationContext;
        init();
    }

    /** do some initiation*/
    public void init(){
        /* some initiation*/
        edit_portrait= (ImageView) activity.findViewById(R.id.edit_portrait);
        edit_text= (EditText) activity.findViewById(R.id.edit_text);
        edit_submit= (Button) activity.findViewById(R.id.edit_submit);
        Picasso.with(activity).load(new File(pic_uri)).into(edit_portrait); //picasso load image

        /* get current calendar */
        Calendar calendar=Calendar.getInstance();
        int monthOfYear=(calendar.get(Calendar.MONTH)+1);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        data_time=dayOfMonth+"/"+monthOfYear;


        /* edit submit set onclick */
        edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToGrowthRecord();
                addToOss(pic_uri);
                long new_id=savaToDB();
                refreshArrayList((int)new_id);
                MyResource.setFragment_show_2("");
                activity.findViewById(R.id.edit).setVisibility(View.GONE);
            }
        });

        MyResource.setFragment_show_2("RecordEdit");
    }

    /** save new record to database */
    public long savaToDB(){
        SQLiteDatabase dbWriter= MyResource.getSqLite().getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("child_id", child_id);
        values.put("pic_uri",pic_uri);
        values.put("date_time",data_time);
        values.put("note", edit_text.getText().toString());
        long new_id=dbWriter.insert(MyResource.getRecord_table(), null, values);
        dbWriter.close();
        return new_id;
    }

    /** refresh record to ui show  */
    public void refreshArrayList(int id){
        ArrayList<RecordJson> array_record= activity.getArray_record();
        RecordJson recordJson=new RecordJson();
        recordJson.setId(id);
        recordJson.setPic_uri(pic_uri);
        recordJson.setDate_time(data_time);
        recordJson.setChild_id(child_id);
        recordJson.setNote(edit_text.getText().toString());
        array_record.add(0, recordJson);
    }

    public void addToOss(String uploadFilePath){
        OssClass ossClass=new OssClass(applicationContext);
        ossClass.putObjectToOss(uploadFilePath);
    }

    public void addToGrowthRecord(){

        /** initiation about requesting for data */
        requestQueue= Volley.newRequestQueue(activity);
        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Customer/addPicUriToDb"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String cloud_id) {
                requestQueue.cancelAll("addGrowthRecordToCloud");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error post");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<String,String>();

                String[] pic_uri_array=pic_uri.split("\\/");
                String pic_uri_str=pic_uri_array[pic_uri_array.length-1];

                Date currentTime=new Date();
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                String datastr=format.format(currentTime);

                map.put("user_id",String.valueOf(getUserId()));
                map.put("type",String.valueOf(1));
                map.put("child_id",String.valueOf(child_id));
                map.put("uri",pic_uri_str);
                map.put("note",edit_text.getText().toString());
                map.put("date",datastr);
                return map;
            }
        };
        request_more.setTag("addGrowthRecordToCloud");
        requestQueue.add(request_more);
    }

    public int getUserId(){

//        int user_id=0;
//        try {
//            File mFile=new File(activity.getFilesDir().getPath()+ BabyInfo.USER_INFO_FOLDER+BabyInfo.USER_INFO);
//            FileInputStream fis= new FileInputStream(mFile);
//            InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
//            char []input=new char[fis.available()];
//            isr.read(input); String inString=new String(input);
//            isr.close();fis.close();
//
//            // parse the string and get customer information in detail
//            Customer customer=JSON.parseObject(inString, Customer.class);
//            user_id =customer.getCus_id();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        SharedPreferences preferences=activity.getSharedPreferences("cus_info",Context.MODE_PRIVATE);
        return preferences.getInt("cus_id",0);
    }

    private ImageView edit_portrait;
    private EditText edit_text;
    private String pic_uri;
    private int child_id;
    private String data_time;
    private Button edit_submit;
    private Child_Time_Record_Activity activity;
    private Context applicationContext;
    private RequestQueue requestQueue;

}
