package com.example.administrator.babygrowth01.familydish;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/22.
 */
public class DishStatic {
    public static int dish_num=0;
    public static HashMap<String,Integer> selected_dish=new HashMap<>();
    public static HashMap<String,Integer> selected_family_dish=new HashMap<>();
    public static ArrayList<DishSelectedTemp> dishSelectedTemps=new ArrayList<>();
    public static int dish_type,dish_id;
    public static boolean dish_first_time =true;
    public static String dish_type_str="fish/";

    public static class DishSelectedTemp{
        private int id;
        private int type;
        private String thumbnail_path;
        private String dish_name;

        public DishSelectedTemp(int id, int type, String thumbnail_path, String dish_name) {
            this.id = id;
            this.type = type;
            this.thumbnail_path = thumbnail_path;
            this.dish_name = dish_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getThumbnail_path() {
            return thumbnail_path;
        }

        public void setThumbnail_path(String thumbnail_path) {
            this.thumbnail_path = thumbnail_path;
        }

        public String getDish_name() {
            return dish_name;
        }

        public void setDish_name(String dish_name) {
            this.dish_name = dish_name;
        }
    }
}
