package com.example.fitnesssocial;

public class ModelComment {
    String cId;
    String comment;
    String ptime;
    String uid;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ModelComment() {
    }

    public ModelComment(String cId, String comment, String ptime, String uid) {
        this.cId = cId;
        this.comment = comment;
        this.ptime = ptime;
        this.uid = uid;
    }
}
