package com.medisyst.medisyst;

import android.view.View;

public class People {
    private String name;
    private String gender;
    private String dob;
    People(String name, String gender, String dob) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getGender() {return gender;}
    public void setGender(String last_date) {this.gender = gender;}
    public String getDob() {return dob;}
    public void setDob(String views) {this.dob = dob;}
}