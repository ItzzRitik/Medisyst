package com.medisyst.medisyst;
public class Permission {
    private String name,key;
    Permission(String name, String key) {
        this.name = name;
        this.key = key;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getKey() {return key;}
    public void setKey(String key) {this.key = key;}
}