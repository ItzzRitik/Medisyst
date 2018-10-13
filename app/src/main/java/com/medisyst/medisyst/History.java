package com.medisyst.medisyst;
public class History {
    private String name,date,pName,solution
    History(String name, String date, String dob,String pName,String solution) {
        this.name = name;
        this.date = date;
        this.dob = dob;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDate() {return date;}
    public void setDate(String gender) {this.date = date;}
    public String getDob() {return dob;}
    public void setDob(String dob) {this.dob = dob;}
}