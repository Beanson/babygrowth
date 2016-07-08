package com.example.administrator.babygrowth01.babyparadise.parentMedia;

/**
 * Created by Administrator on 2016/2/19.
 */
public class ParentMediaJson {

    private int id,type,age_layer;
    private String thumbnail_uri,depict,media_uri;

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

    public int getAge_layer() {
        return age_layer;
    }

    public void setAge_layer(int age_layer) {
        this.age_layer = age_layer;
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

    public String getMedia_uri() {
        return media_uri;
    }

    public void setMedia_uri(String media_uri) {
        this.media_uri = media_uri;
    }
}
