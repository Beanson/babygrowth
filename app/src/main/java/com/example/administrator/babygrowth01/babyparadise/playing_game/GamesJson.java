package com.example.administrator.babygrowth01.babyparadise.playing_game;

/**
 * Created by Administrator on 2016/2/19.
 */
public class GamesJson {

    private int id;
    private String game_name;
    private String game_thumbnail_uri;
    private String game_uri;
    private int proper_age;
    private String game_depict;
    private int hot_mark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_thumbnail_uri() {
        return game_thumbnail_uri;
    }

    public void setGame_thumbnail_uri(String game_thumbnail_uri) {
        this.game_thumbnail_uri = game_thumbnail_uri;
    }

    public String getGame_uri() {
        return game_uri;
    }

    public void setGame_uri(String game_uri) {
        this.game_uri = game_uri;
    }

    public int getProper_age() {
        return proper_age;
    }

    public void setProper_age(int proper_age) {
        this.proper_age = proper_age;
    }

    public String getGame_depict() {
        return game_depict;
    }

    public void setGame_depict(String game_depict) {
        this.game_depict = game_depict;
    }

    public int getHot_mark() {
        return hot_mark;
    }

    public void setHot_mark(int hot_mark) {
        this.hot_mark = hot_mark;
    }
}
