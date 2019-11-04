package com.ssc.sscappadmin.Model;

public class Companies {

    public Companies(String name) {
        this.name = name;
    }


    private String name,uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Companies(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public Companies() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
