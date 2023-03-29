package com.example.fitnesssocial;

public class ModelPost {
    String ptime, pcomments;
    String title;
    String uid;
    String chooseImg;
    String mapImg;
    String plike;
    String description;
    String etype;
    float total_cals;
    public ModelPost() {
    }

    public ModelPost(String ptime, String pcomments, String title, String uid, String chooseImg, String mapImg, String plike, String description, String etype, float total_cals) {
        this.ptime = ptime;
        this.pcomments = pcomments;
        this.title = title;
        this.uid = uid;
        this.chooseImg = chooseImg;
        this.mapImg = mapImg;
        this.plike = plike;
        this.description = description;
        this.etype = etype;
        this.total_cals = total_cals;
    }

    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
    }

    public float getTotal_cals() {
        return total_cals;
    }

    public void setTotal_cals(float total_cals) {
        this.total_cals = total_cals;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPcomments() {
        return pcomments;
    }

    public void setPcomments(String pcomments) {
        this.pcomments = pcomments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChooseImg() {
        return chooseImg;
    }

    public void setChooseImg(String chooseImg) {
        this.chooseImg = chooseImg;
    }

    public String getMapImg() {
        return mapImg;
    }

    public void setMapImg(String mapImg) {
        this.mapImg = mapImg;
    }

    public String getPlike() {
        return plike;
    }

    public void setPlike(String plike) {
        this.plike = plike;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
