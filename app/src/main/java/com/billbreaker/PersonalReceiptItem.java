package com.billbreaker;

import java.io.Serializable;

class PersonalReceiptItem implements Serializable {

    String name;
    double price;

    public PersonalReceiptItem(String name, double price) {
        this.name = name;
        this.price = price;
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
