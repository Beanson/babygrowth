package com.example.administrator.babygrowth01.babyparadise.babyRecipe;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyrecords.Main.PortraitJson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/25.
 */
public class LoadBabyAsyncRecipe extends AsyncTask<Void,Void,Void>{


    /**
     * constructor
     * @param ll_portrait_recipe  linearLayout to add portrait dynamically
     * @param baby_recipe_detail recyclerview to add recipe from internet data
     * @param activity
     */
    public LoadBabyAsyncRecipe(LinearLayout ll_portrait_recipe, RecyclerView baby_recipe_detail, Activity activity) {
        this.ll_portrait_recipe = ll_portrait_recipe;
        this.baby_recipe_detail = baby_recipe_detail;
        this.activity = activity;
        requestQueue= Volley.newRequestQueue(activity);
    }

    /**
     * query local database and get children information
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {

        // get child information from database
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

    /**
     * after load data from database
     * add portrait information to widgets
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // add portrait data
        addPortrait();

        //after portrait load we load recipe data corresponded to specific age
        acquireDataVolley();
    }


    /**
     * add child portrait to widgets dynamically
     */
    public void addPortrait(){

        //display children information to widgets
        for (PortraitJson ptjson :
                ptjsonArray) {

            RelativeLayout view= (RelativeLayout) RelativeLayout.inflate(activity, R.layout.baby_recipe_portrait,null);
            String file_path=ptjson.getPortrait_uri();/* load the portrait uri*/
            view.setTag(R.id.rv_tag_1, ptjson.getChild_id());/* add id for marking which baby icon the user are clicking */

            //set child portrait image
            ImageView imageView= (ImageView) view.findViewById(R.id.iv_baby_recipe_portrait); /* find the imageview*/
            Picasso.with(activity).load(new File(file_path)).transform(new CircleTransform()).into(imageView);/* set child image to the round imageview*/

            //set child name
            TextView textName= (TextView) view.findViewById(R.id.baby_recipe_name);
            textName.setText(ptjson.getNick_name());

            //set child age
            //TextView textAge= (TextView) view.findViewById(R.id.baby_recipe_age);
            String data[]=ptjson.getBirth().split("\\-");
            String year=data[0]; String month=data[1];

            //get current data
            Calendar calendar=Calendar.getInstance();
            int current_year=calendar.get(Calendar.YEAR);
            int current_month=(calendar.get(Calendar.MONTH)+1);

            //calculate time length from child's birth to now
            total_month=(current_year-Integer.parseInt(year))*12+current_month-Integer.parseInt(month);
//            int age_year=total_month/12;int age_month=total_month%12;
//            String age_text=null;
//            if(age_year>0) age_text=age_year+"年";
//            if(age_month>0) age_text=age_text+age_month+"个月";

            //set age to text widget
            //textAge.setText(age_text);

            ll_portrait_recipe.addView(view, 0);
        }
    }

    /**
     * get recipe data by volley http request
     */
    public void acquireDataVolley(){
        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Recipe/getRecipe"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                List<RecipeJson> results= JSON.parseArray(s, RecipeJson.class);
                ArrayList<RecipeJson> arraylist=new ArrayList<>();
                arraylist.addAll(results);
                displayToWidget(arraylist);

                requestQueue.cancelAll("seek_recipe_data");
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
                map.put("total_month",String.valueOf(total_month));
                return map;
            }
        };
        request_more.setTag("seek_recipe_data");
        requestQueue.add(request_more);
    }

    /**
     * display recipe data to widgets
     * @param recipeJsons data received from http request
     */
    public void displayToWidget(ArrayList<RecipeJson> recipeJsons){

        RecyclerViewHeader header = RecyclerViewHeader.fromXml(activity, R.layout.baby_recipe_recycler_header);
        // remember to set LayoutManager for your RecyclerView before add header add to recyclerview
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        baby_recipe_detail.setLayoutManager(linearLayoutManager);
        header.attachTo(baby_recipe_detail);

        RecipeListRecycleAdapter recipeAdapter = new RecipeListRecycleAdapter(recipeJsons, activity);
        baby_recipe_detail.setAdapter(recipeAdapter);

    }

    private LinearLayout ll_portrait_recipe;
    private RecyclerView baby_recipe_detail;
    private Activity activity;

    public int total_month;
    private RequestQueue requestQueue;
    private ArrayList<PortraitJson> ptjsonArray=new ArrayList<>();
}
