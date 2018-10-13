package com.medisyst.medisyst;
public class History {
    private String name,date,profName,docName,solution;
    History(String name, String date,String profName,String docName,String solution) {
        this.name = name;
        this.date = date;
        this.docName = docName;
        this.profName = profName;
        this.solution = solution;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public String getdocName() {return docName;}
    public void setdocName(String date) {this.docName = docName;}
    public String getProfName() {return profName;}
    public void setProfName(String profName) {this.profName = profName;}
    public String getSolution() {return solution;}
    public void setSolution(String solution) {this.solution = solution;}
}