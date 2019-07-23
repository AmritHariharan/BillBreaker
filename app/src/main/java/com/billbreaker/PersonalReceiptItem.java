package com.billbreaker;

import java.io.Serializable;

/**
 * Data class to hold name of the person and the price the person needs to pay for the receipt
 */
class PersonalReceiptItem implements Serializable {

    private String name;
    private double price;

    PersonalReceiptItem(String name, double price) {
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
