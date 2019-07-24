package com.billbreaker;

import java.io.Serializable;
import java.util.List;

public class Receipt implements Serializable {

    private List<PersonalReceiptItem> personalReceiptItems;
    private long timestamp;
    private double subtotal;
    private double tax;
    private double tip;

    public Receipt(List<PersonalReceiptItem> personalReceiptItems, long timestamp, double subtotal,
                   double tax, double tip) {
        this.personalReceiptItems = personalReceiptItems;
        this.timestamp = timestamp;
        this.subtotal = subtotal;
        this.tax = tax;
        this.tip = tip;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public List<PersonalReceiptItem> getPersonalReceiptItems() {
        return personalReceiptItems;
    }

    public void setPersonalReceiptItems(List<PersonalReceiptItem> personalReceiptItems) {
        this.personalReceiptItems = personalReceiptItems;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
