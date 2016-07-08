package com.example.administrator.babygrowth01.babyparadise.babyRaise;

/**
 * Created by Administrator on 2016/5/28.
 */
public class ExpertSuJson {

    private int id;
    private String thumbnail_uri;
    private String media_uri;
    private String depict;
    private int type;
    private int age_begin;
    private int age_end;
    private int times;

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

    public String getMedia_uri() {
        return media_uri;
    }

    public void setMedia_uri(String media_uri) {
        this.media_uri = media_uri;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAge_begin() {
        return age_begin;
    }

    public void setAge_begin(int age_begin) {
        this.age_begin = age_begin;
    }

    public int getAge_end() {
        return age_end;
    }

    public void setAge_end(int age_end) {
        this.age_end = age_end;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
