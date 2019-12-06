package com.ssc.sscappadmin.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Nullable;

public class Companies implements Parcelable {

    private String name, uid;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Companies o1 = (Companies) obj;

        if (!TextUtils.isEmpty(this.getName()) && !TextUtils.isEmpty(o1.getName()))
            return o1.getName().equals(this.getName());
        else
            return false;
    }
}
