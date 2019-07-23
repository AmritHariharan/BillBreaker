package com.billbreaker;

import android.os.Parcel;
import android.os.Parcelable;

class ReceiptItem implements Parcelable {
    private String name;
    private double price;

    ReceiptItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    ReceiptItem(Parcel in) {
        this.name = in.readString();
        this.price = in.readDouble();
    }

    public static final Parcelable.Creator<ReceiptItem> CREATOR
            = new Parcelable.Creator<ReceiptItem>() {
        public ReceiptItem createFromParcel(Parcel in) {
            return new ReceiptItem(in);
        }

        public ReceiptItem[] newArray(int size) {
            return new ReceiptItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.name);
        parcel.writeDouble(this.price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
