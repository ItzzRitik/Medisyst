package com.medisyst.medisyst;
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
    public void setGender(String gender) {this.gender = gender;}
    public String getDob() {return dob;}
    public void setDob(String dob) {this.dob = dob;}
}