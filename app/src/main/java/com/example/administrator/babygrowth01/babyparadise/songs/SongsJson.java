package com.example.administrator.babygrowth01.babyparadise.songs;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */
public class SongsJson {

    private String name;
    private int status;
    private int play_times;
    private String thumbnail_uri;
    private String http_uri;
    private String local_uri;

    /*private int _id;
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPlay_times() {
        return play_times;
    }

    public void setPlay_times(int play_times) {
        this.play_times = play_times;
    }

    public String getThumbnail_uri() {
        return thumbnail_uri;
    }

    public void setThumbnail_uri(String thumbnail_uri) {
        this.thumbnail_uri = thumbnail_uri;
    }

    public String getHttp_uri() {
        return http_uri;
    }

    public void setHttp_uri(String http_uri) {
        this.http_uri = http_uri;
    }

    public String getLocal_uri() {
        return local_uri;
    }

    public void setLocal_uri(String local_uri) {
        this.local_uri = local_uri;
    }
}
