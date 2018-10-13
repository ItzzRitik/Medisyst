package com.medisyst.medisyst;
public class History {
    private String name,date,profName,solution;
    History(String name, String date, String dob,String profName,String solution) {
        this.name = name;
        this.date = date;
        this.profName = profName;
        this.solution = solution;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public String getProfName() {return profName;}
    public void setProfName(String profName) {this.profName = profName;}
    public String getSolution() {return solution;}
    public void setSolution(String solution) {this.solution = solution;}
}