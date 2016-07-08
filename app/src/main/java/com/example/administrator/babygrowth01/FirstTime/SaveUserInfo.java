package com.example.administrator.babygrowth01.FirstTime;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.administrator.babygrowth01.babyrecords.Main.BabyInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/4/7.
 */
public class SaveUserInfo extends AsyncTask<String,Void,Void> {

    /** save the user message to a file*/
    private Activity activity;

    public SaveUserInfo(Activity activity){
        this.activity=activity;
    }
    @Override
    protected Void doInBackground(String... params) {

        // get the three user information ready to insert to database
//        JSONObject jsonObject=new JSONObject();
//        try {
//            // for writing customer information json to the file
//            jsonObject.put("cus_id",Integer.parseInt(params[0]));
//            jsonObject.put("cus_name",params[1]);
//            jsonObject.put("cus_mail",params[2]);
//            jsonObject.put("cus_phone",params[3]);
//            jsonObject.put("cus_account",params[4]);
//            jsonObject.put("cus_password",params[5]);
//            jsonObject.put("cus_qq_link",params[6]);
//
//            System.out.println(jsonObject.toString());
//            File user_info=new File(activity.getFilesDir().getPath()+ BabyInfo.USER_INFO_FOLDER+BabyInfo.USER_INFO);
//            FileOutputStream fos=new FileOutputStream(user_info);
//            OutputStreamWriter osw=new OutputStreamWriter(fos);
//            osw.write(jsonObject.toString());
//            osw.flush();fos.flush();
//            osw.close();fos.close();
//
//            // for reading files
//            /*FileInputStream fis=new FileInputStream(user_info);
//            InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
//            char []input=new char[fis.available()];
//            isr.read(input);
//            isr.close();fis.close();
//            String inString=new String(input);*/
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return null;
    }
}
