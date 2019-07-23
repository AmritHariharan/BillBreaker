package com.billbreaker;

import java.util.List;

public class Receipt {

    private List<PersonalReceiptItem> personalReceiptItems;
    private long timestamp;

    public Receipt(List<PersonalReceiptItem> personalReceiptItems, long timestamp) {
        this.personalReceiptItems = personalReceiptItems;
        this.timestamp = timestamp;
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
