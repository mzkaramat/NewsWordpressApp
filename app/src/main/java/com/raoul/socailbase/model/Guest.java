package com.raoul.socailbase.model;

/**
 * Created by mobile_perfect on 10/04/15.
 */
public class Guest {

    String userid;
    String imageurl;
    String username;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getUsername() {
        return username;
    }
}
