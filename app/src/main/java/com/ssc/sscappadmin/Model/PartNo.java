package com.ssc.sscappadmin.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class PartNo implements Parcelable {

    public String name, ssc_code, reference, model, image, cost_price, companyname;
    boolean visibility, selected = false;

    public PartNo(String name, String ssc_code, String reference, String model, String image, String cost_price, String companyname, boolean visibility) {
        this.name = name;
        this.ssc_code = ssc_code;
        this.reference = reference;
        this.model = model;
        this.image = image;
        this.cost_price = cost_price;
        this.companyname = companyname;
        this.visibility = visibility;

    }

    protected PartNo(Parcel in) {
        name = in.readString();
        ssc_code = in.readString();
        reference = in.readString();
        model = in.readString();
        image = in.readString();
        cost_price = in.readString();
        companyname = in.readString();
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
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


    public PartNo(String name, String ssc_code, String reference, String model, String image, String cost_price, String companyname, boolean visibility, boolean selected) {
        this.name = name;
        this.ssc_code = ssc_code;
        this.reference = reference;
        this.model = model;
        this.image = image;
        this.cost_price = cost_price;
        this.companyname = companyname;
        this.visibility = visibility;
        this.selected = selected;
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
        parcel.writeString(reference);
        parcel.writeString(model);
        parcel.writeString(image);
        parcel.writeString(cost_price);
        parcel.writeString(companyname);
        parcel.writeByte((byte) (visibility ? 1 : 0));
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}
