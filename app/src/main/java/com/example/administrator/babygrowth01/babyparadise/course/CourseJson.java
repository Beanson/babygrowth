package com.example.administrator.babygrowth01.babyparadise.course;

/**
 * Created by Administrator on 2016/2/19.
 */
public class CourseJson {
    private int id;
    private String thumbnail_uri;
    private String name;
    private String learn_content;
    private String video_uri;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLearn_content() {
        return learn_content;
    }

    public void setLearn_content(String learn_content) {
        this.learn_content = learn_content;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }
}
