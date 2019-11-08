package com.ssc.sscappadmin.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Companies implements Parcelable {

    public Companies(String name) {
        this.name = name;
    }


    private String name,uid;

    protected Companies(Parcel in) {
        name = in.readString();
        uid = in.readString();
    }

    public static final Creator<Companies> CREATOR = new Creator<Companies>() {
        @Override
        public Companies createFromParcel(Parcel in) {
            return new Companies(in);
        }

        @Override
        public Companies[] newArray(int size) {
            return new Companies[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(uid);
    }
}
