package com.example.administrator.babygrowth01.babyparadise.babyRecipe;

/**
 * Created by Administrator on 2016/2/19.
 */
public class RecipeJson {

    private int id;
    private String recipe_name;
    private String recipe_depict;
    private String stuffs;
    private String recipe_thumbnail;
    private String recipe_video;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_depict() {
        return recipe_depict;
    }

    public void setRecipe_depict(String recipe_depict) {
        this.recipe_depict = recipe_depict;
    }

    public String getStuffs() {
        return stuffs;
    }

    public void setStuffs(String stuffs) {
        this.stuffs = stuffs;
    }

    public String getRecipe_thumbnail() {
        return recipe_thumbnail;
    }

    public void setRecipe_thumbnail(String recipe_thumbnail) {
        this.recipe_thumbnail = recipe_thumbnail;
    }

    public String getRecipe_video() {
        return recipe_video;
    }

    public void setRecipe_video(String recipe_video) {
        this.recipe_video = recipe_video;
    }
}
