package com.example.administrator.babygrowth01.babyrecords.Main;

/**
 * Created by Administrator on 2016/2/29.
 */
public class RecordJson {
    private int id;
    private int child_id;
    private String pic_uri;
    private String note;
    private String date_time;
    private int height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChild_id() {
        return child_id;
    }

    public void setChild_id(int child_id) {
        this.child_id = child_id;
    }

    public String getPic_uri() {
        return pic_uri;
    }

    public void setPic_uri(String pic_uri) {
        this.pic_uri = pic_uri;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
