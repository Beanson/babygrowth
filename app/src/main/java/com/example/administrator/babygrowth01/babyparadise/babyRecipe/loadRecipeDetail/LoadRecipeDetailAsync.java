package com.example.administrator.babygrowth01.babyparadise.babyRecipe.loadRecipeDetail;

import android.os.AsyncTask;
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
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/26.
 */
public class LoadRecipeDetailAsync {

    public LoadRecipeDetailAsync(String id,BabyRecipeDetailActivity activity,LinearLayout linearLayout, RequestQueue requestQueue) {
        this.activity = activity;
        this.linearLayout=linearLayout;
        this.requestQueue = requestQueue;

        loadRecipeSteps(id);
    }

    /**
     * load steps data
     * @param recipe_id request data by giving recipe_id
     */
    public void loadRecipeSteps(final String recipe_id){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Recipe/getRecipeDetail"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                List<RecipeDetailJson> results= JSON.parseArray(s, RecipeDetailJson.class);
                showRecipeStepToWidget(results); //show recipe making steps to widgets
                requestQueue.cancelAll("seek_recipe_detail");
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
                map.put("recipe_id",recipe_id);
                return map;
            }
        };
        request_more.setTag("seek_recipe_detail");
        requestQueue.add(request_more);
    }


    /**
     * initiate widgets layout and add to linearLayout dynamically
     * @param recipeStep contain data ready to add to widgets layout
     */
    public void showRecipeStepToWidget(List<RecipeDetailJson> recipeStep){

        for (RecipeDetailJson recipe :
                recipeStep) {

            // initiate widgets layout ready to add to linearLayout
            LinearLayout view= (LinearLayout) LinearLayout.inflate(activity, R.layout.baby_recipe_step_detail,null);
            ImageView imageView= (ImageView) view.findViewById(R.id.iv_recipe_step);
            TextView textView= (TextView) view.findViewById(R.id.tv_recipe_step);

            // set data to widgets
            textView.setText(recipe.getAction_depict());
            String recipe_detail_thumbnail_base="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Recipe/supplementary/thumbnail_step/";
            String pic_uri=recipe_detail_thumbnail_base+recipe.getThumbnail();
            //System.out.println("pic mark: "+pic_uri);
            Picasso.with(activity).load(pic_uri).into(imageView);

            // add layout widgets to linearLayout
            linearLayout.addView(view);
        }
    }

    private BabyRecipeDetailActivity activity;
    private RequestQueue requestQueue;
    private LinearLayout linearLayout;
    //private ArrayList<RecipeDetailJson> recipeDetailJsons=new ArrayList<RecipeDetailJson>();
}
