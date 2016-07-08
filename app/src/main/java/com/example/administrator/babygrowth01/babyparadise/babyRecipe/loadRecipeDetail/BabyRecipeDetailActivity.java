package com.example.administrator.babygrowth01.babyparadise.babyRecipe.loadRecipeDetail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

public class BabyRecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // receive data from recipe
        Intent intent=getIntent();
        String id= (String) intent.getExtras().get("id");
        String recipe_name= (String) intent.getExtras().get("recipe_name");
        String recipe_depict= (String) intent.getExtras().get("recipe_depict");
        String stuffs= (String) intent.getExtras().get("stuffs");
        String recipe_thumbnail= (String) intent.getExtras().get("recipe_thumbnail");
        String recipe_video= (String) intent.getExtras().get("recipe_video");

        setContentView(R.layout.activity_baby_recipe_detail);
        initWidget();
        initData(id,recipe_name, recipe_depict, stuffs, recipe_thumbnail, recipe_video);

    }

    /**
     * fill in widgets with data
     */
    public void initData(String id,String recipe_name,String recipe_depict,String stuffs,String recipe_thumbnail,String recipe_video){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        baby_recipe_name.setText(recipe_name);
        baby_recipe_detail.setText(recipe_depict);
        recipe_stuff.setText(stuffs);
        String recipe_thumbnail_base="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Recipe/supplementary/thumbnail_main/";
        Picasso.with(this).load(recipe_thumbnail_base+recipe_thumbnail).into(iv_recipe_image);
        new LoadRecipeDetailAsync(id,this,ll_recipe_steps, requestQueue);
    }

    /**
     * initiate those widgets
     */
    public void initWidget(){
        iv_recipe_image= (ImageView) findViewById(R.id.iv_recipe_image);
        baby_recipe_name= (TextView) findViewById(R.id.baby_recipe_name);
        baby_recipe_detail= (TextView) findViewById(R.id.baby_recipe_detail);
        recipe_stuff= (TextView) findViewById(R.id.recipe_stuff);
        ll_recipe_steps= (LinearLayout) findViewById(R.id.ll_recipe_steps);
    }

    private ImageView iv_recipe_image;
    private TextView baby_recipe_name,baby_recipe_detail,recipe_stuff;
    private LinearLayout ll_recipe_steps;
}
