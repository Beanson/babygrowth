package com.example.administrator.babygrowth01.babyrecords.Main;

/**
 * Created by Administrator on 2016/2/29.
 */
public class PortraitJson {
    public int child_id;
    private String portrait_uri;
    private String nick_name;
    private String gender;
    private String birth;


    public int getChild_id() {
        return child_id;
    }

    public void setChild_id(int child_id) {
        this.child_id = child_id;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

}
