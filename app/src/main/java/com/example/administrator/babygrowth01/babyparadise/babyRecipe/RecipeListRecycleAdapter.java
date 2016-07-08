package com.example.administrator.babygrowth01.babyparadise.babyRecipe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.babyRecipe.loadRecipeDetail.BabyRecipeDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class RecipeListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<RecipeJson> arrayList;
    private Activity activity;

    public RecipeListRecycleAdapter(ArrayList<RecipeJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.baby_recipe_each,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        /** binding the id as tag with the view */ vh.getView().setTag(R.id.rv_tag_1, arrayList.get(position).getId());
        String recipe_thumbnail_base="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Recipe/supplementary/thumbnail_main/";
        vh.getTv_recipe_name().setText(arrayList.get(position).getRecipe_name());
        vh.getTv_recipe_depict().setText((arrayList.get(position).getRecipe_depict().substring(0,32))+"...");
        Picasso.with(activity).load(recipe_thumbnail_base+arrayList.get(position).getRecipe_thumbnail()).transform(new CircleTransform()).into(vh.getIv_recipe_image());
    }


    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    /**
     * holder identify the content widgets
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView iv_recipe_image;
        private TextView tv_recipe_name, tv_recipe_depict;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            iv_recipe_image =(ImageView)itemView.findViewById(R.id.iv_recipe_image);
            tv_recipe_name = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            tv_recipe_depict = (TextView) itemView.findViewById(R.id.tv_recipe_depict);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id=view.getTag(R.id.rv_tag_1).toString();
                    RecipeJson recipeJson=arrayList.get(Integer.parseInt(id));
                    Intent intent=new Intent(activity,BabyRecipeDetailActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("recipe_name",recipeJson.getRecipe_name());
                    intent.putExtra("recipe_depict",recipeJson.getRecipe_depict());
                    intent.putExtra("stuffs",recipeJson.getStuffs());
                    intent.putExtra("recipe_thumbnail",recipeJson.getRecipe_thumbnail());
                    intent.putExtra("recipe_video",recipeJson.getRecipe_video());
                    activity.startActivity(intent);
                }
            });
        }

        public View getView(){return view;}

        public ImageView getIv_recipe_image() {
            return iv_recipe_image;
        }

        public TextView getTv_recipe_name() {
            return tv_recipe_name;
        }

        public TextView getTv_recipe_depict() {
            return tv_recipe_depict;
        }
    }
}
