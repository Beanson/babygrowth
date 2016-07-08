package com.example.administrator.babygrowth01.personalInfo;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyrecords.Main.PortraitJson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/22.
 */
public class LoadBabyAsync extends AsyncTask<Void,Void,Void> {

    private ArrayList<PortraitJson> ptjsonArray;
    private LinearLayout ly_babyInfo;
    private Activity activity;

    public LoadBabyAsync(ArrayList<PortraitJson> ptjsonArray,LinearLayout ly_babyInfo, Activity activity) {
        this.ptjsonArray=ptjsonArray;
        this.ly_babyInfo = ly_babyInfo;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {

        SQLiteDatabase dbReader= MyResource.getSqLite().getReadableDatabase();
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
                ptjsonArray.add(0,portraitJson);
            }while (cursor.moveToNext());
            cursor.close();
        }
        dbReader.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //System.out.println("mark             "+ptjsonArray.size());
        for (PortraitJson ptjson :
                ptjsonArray) {

            RelativeLayout view= (RelativeLayout) RelativeLayout.inflate(activity, R.layout.child_item_each,null);
            String file_path=ptjson.getPortrait_uri();/* load the portrait uri*/
            view.setTag(R.id.rv_tag_1, ptjson.getChild_id());/* add id for marking which baby icon the user are clicking */

            //set child portrait image
            ImageView imageView= (ImageView) view.findViewById(R.id.iv_portrait); /* find the imageview*/
            Picasso.with(activity).load(new File(file_path)).transform(new CircleTransform()).into(imageView);/* set child image to the round imageview*/

            //set child name
            TextView textName= (TextView) view.findViewById(R.id.tv_child_name);
            textName.setText(ptjson.getNick_name());

            //set child birth
            TextView textBirth= (TextView) view.findViewById(R.id.tv_child_birth);
            textBirth.setText(ptjson.getBirth());

            ly_babyInfo.addView(view, 0);
        }
    }
}
