 package com.example.administrator.babygrowth01.babyparadise.babyRecipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.babyRecipe.loadRecipeDetail.LoadRecipeDetailAsync;

 public class BabyRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_recipe);
        initWidget();
        initPortrait();
    }

     /**
      * initiation of children portrait and their click items
      */
     public void initPortrait(){
        // get child information from database name,pic_uri,age and deal with the following things after that
         new LoadBabyAsyncRecipe(ll_portrait_recipe,baby_recipe_detail,this).execute();

     }

     /**
      * initiation of widgets
      */
     public void initWidget(){
         ll_portrait_recipe= (LinearLayout) findViewById(R.id.ll_portrait_recipe);
         baby_recipe_detail= (RecyclerView) findViewById(R.id.baby_recipe_detail);
     }



     private LinearLayout ll_portrait_recipe;
     private RecyclerView baby_recipe_detail;

}
