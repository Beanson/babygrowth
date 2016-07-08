package com.example.administrator.babygrowth01.babyparadise.clothesMall;

/**
 * Created by Administrator on 2016/2/19.
 */
public class ClothesJson {

    private ClothesJsonEach clothes_01;
    private ClothesJsonEach clothes_02;

    public ClothesJsonEach getClothes_01() {
        return clothes_01;
    }

    public void setClothes_01(ClothesJsonEach clothes_01) {
        this.clothes_01 = clothes_01;
    }

    public ClothesJsonEach getClothes_02() {
        return clothes_02;
    }

    public void setClothes_02(ClothesJsonEach clothes_02) {
        this.clothes_02 = clothes_02;
    }

    public class ClothesJsonEach{

        private int id;
        private String thumbnail_uri;
        private String depict;
        private String price;
        private String month_sell;
        private String age_layer;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getThumbnail_uri() {
            return thumbnail_uri;
        }

        public void setThumbnail_uri(String thumbnail_uri) {
            this.thumbnail_uri = thumbnail_uri;
        }

        public String getDepict() {
            return depict;
        }

        public void setDepict(String depict) {
            this.depict = depict;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMonth_sell() {
            return month_sell;
        }

        public void setMonth_sell(String month_sell) {
            this.month_sell = month_sell;
        }

        public String getAge_layer() {
            return age_layer;
        }

        public void setAge_layer(String age_layer) {
            this.age_layer = age_layer;
        }
    }

}
