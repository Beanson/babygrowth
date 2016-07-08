package com.example.administrator.babygrowth01.familydish;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/27.
 */
public class GetDishDetailVolly{

    public GetDishDetailVolly(Activity activity,TextView dish_name,ImageView dish_image,TextView dish_depict,TextView dish_material,TextView dish_step,ImageView iv_dish_media) {

        this.activity=activity;
        this.dish_name=dish_name;
        this.dish_depict=dish_depict;
        this.dish_material=dish_material;
        this.dish_step=dish_step;
        this.dish_image=dish_image;
        this.iv_dish_media=iv_dish_media;

        requestQueue= Volley.newRequestQueue(activity);
    }

    /** when the user like to seek more optional data from the internet */
    public void loadOptionalData(final int dish_type,final int dish_id,final String dish_type_str){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Dish/getDishDetail"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String thumbnail_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Dish/options_dish/";
                String video_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Dish/family_dish/video/qing-dan-yu-tou-tang.mp4";
                DishJson result=JSON.parseObject(s,DishJson.class);
                //result.setDish_video_uri(video_uri_base_root);
                iv_dish_media.setTag(video_uri_base_root);
                requestQueue.cancelAll("seek_dish_detail");

                dish_name.setText(result.getDish_name());
                Picasso.with(activity).load(thumbnail_uri_base_root + dish_type_str+"thumbnail/"+result.getDish_thumbnail_uri()).into(dish_image);
                dish_depict.setText(result.getDish_depict());
                dish_material.setText(result.getDish_stuff().replace("|", "\n"));
                dish_step.setText(result.getDish_step().replace("|","\n"));

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
                map.put("dish_id",String.valueOf(dish_id));
                map.put("dish_type",String.valueOf(dish_type));
                return map;
            }
        };
        request_more.setTag("seek_dish_detail");
        requestQueue.add(request_more);
    }

    private Activity activity;
    private RequestQueue requestQueue;
    private TextView dish_name,dish_material,dish_step,dish_depict;
    private ImageView dish_image,iv_dish_media;
}
