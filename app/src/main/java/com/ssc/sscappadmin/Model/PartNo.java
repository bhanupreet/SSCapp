package com.ssc.sscappadmin.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Nullable;

public class PartNo implements Parcelable {

    public String name;
    public String ssc_code;
    public String image;

    public PartNo(String name, String ssc_code, String image, String companyname, String uid, boolean visibility, boolean selected) {
        this.name = name;
        this.ssc_code = ssc_code;
        this.image = image;
        this.companyname = companyname;
        this.uid = uid;
        this.visibility = visibility;
        this.selected = selected;
    }

    protected PartNo(Parcel in) {
        name = in.readString();
        ssc_code = in.readString();
        image = in.readString();
        companyname = in.readString();
        uid = in.readString();
        visibility = in.readByte() != 0;
        selected = in.readByte() != 0;
    }



    public static final Creator<PartNo> CREATOR = new Creator<PartNo>() {
        @Override
        public PartNo createFromParcel(Parcel in) {
            return new PartNo(in);
        }

        @Override
        public PartNo[] newArray(int size) {
            return new PartNo[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String companyname;


    public String uid;
    boolean visibility, selected = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsc_code() {
        return ssc_code;
    }

    public void setSsc_code(String ssc_code) {
        this.ssc_code = ssc_code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public PartNo() {
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        PartNo o1 = (PartNo) obj;

        if (!TextUtils.isEmpty(this.getSsc_code()) && !TextUtils.isEmpty(o1.getSsc_code()))
            return o1.getSsc_code().equals(this.getSsc_code());
        else
            return false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(ssc_code);
        parcel.writeString(image);
        parcel.writeString(companyname);
        parcel.writeString(uid);
        parcel.writeByte((byte) (visibility ? 1 : 0));
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}
