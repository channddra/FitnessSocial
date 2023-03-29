package com.example.fitnesssocial;

public class ModelHistory {
    String timestamp;
    String total_dist;
    String total_steps;
    String total_cals;

    public ModelHistory(){

    }
    public ModelHistory(String timestamp, String total_dist, String total_steps, String total_cals) {
        this.timestamp = timestamp;
        this.total_dist = total_dist;
        this.total_steps = total_steps;
        this.total_cals = total_cals;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTotal_dist() {
        return total_dist;
    }

    public void setTotal_dist(String total_dist) {
        this.total_dist = total_dist;
    }

    public String getTotal_steps() {
        return total_steps;
    }

    public void setTotal_steps(String total_steps) {
        this.total_steps = total_steps;
    }

    public String getTotal_cals() {
        return total_cals;
    }

    public void setTotal_cals(String total_cals) {
        this.total_cals = total_cals;
    }
}
