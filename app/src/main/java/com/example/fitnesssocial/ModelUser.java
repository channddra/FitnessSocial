package com.example.fitnesssocial;

public class ModelUser {
    String name;
    String email;
    String image;
    String uid;
    String gender;

    int dob_day;
    int dob_month;
    int dob_year;

    float walk_dist;
    float walk_steps;
    float walk_cals;
    float run_dist;
    float run_cals;
    float cycle_dist;
    float cycle_cals;
    float weight;
    float height;

    public float getWalk_dist() {
        return walk_dist;
    }

    public void setWalk_dist(float walk_dist) {
        this.walk_dist = walk_dist;
    }

    public float getWalk_steps() {
        return walk_steps;
    }

    public void setWalk_steps(float walk_steps) {
        this.walk_steps = walk_steps;
    }

    public float getWalk_cals() {
        return walk_cals;
    }

    public void setWalk_cals(float walk_cals) {
        this.walk_cals = walk_cals;
    }

    public float getRun_dist() {
        return run_dist;
    }

    public void setRun_dist(float run_dist) {
        this.run_dist = run_dist;
    }

    public float getRun_cals() {
        return run_cals;
    }

    public void setRun_cals(float run_cals) {
        this.run_cals = run_cals;
    }

    public float getCycle_dist() {
        return cycle_dist;
    }

    public void setCycle_dist(float cycle_dist) {
        this.cycle_dist = cycle_dist;
    }

    public float getCycle_cals() {
        return cycle_cals;
    }

    public void setCycle_cals(float cycle_cals) {
        this.cycle_cals = cycle_cals;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDob_day() {
        return dob_day;
    }

    public void setDob_day(int dob_day) {
        this.dob_day = dob_day;
    }

    public int getDob_month() {
        return dob_month;
    }

    public void setDob_month(int dob_month) {
        this.dob_month = dob_month;
    }

    public int getDob_year() {
        return dob_year;
    }

    public void setDob_year(int dob_year) {
        this.dob_year = dob_year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ModelUser() {
    }

    public ModelUser(String name, String email, String image, String uid, String gender, int dob_day, int dob_month, int dob_year, float walk_dist, float walk_steps, float walk_cals, float run_dist, float run_cals, float cycle_dist, float cycle_cals, float weight, float height) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.uid = uid;
        this.gender = gender;
        this.dob_day = dob_day;
        this.dob_month = dob_month;
        this.dob_year = dob_year;
        this.walk_dist = walk_dist;
        this.walk_steps = walk_steps;
        this.walk_cals = walk_cals;
        this.run_dist = run_dist;
        this.run_cals = run_cals;
        this.cycle_dist = cycle_dist;
        this.cycle_cals = cycle_cals;
        this.weight = weight;
        this.height = height;
    }
}
