package com.billbreaker;

import java.util.ArrayList;
import java.util.List;

public class ReceiptItemBreakdown {
    private String name;
    private double price;
    private List<String> people = new ArrayList<>();

    public ReceiptItemBreakdown(ReceiptItem item) {
        this.name = item.getName();
        this.price = item.getPrice();
    }

    public ReceiptItemBreakdown(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void editPerson(String personName) {
        if (people.contains(personName))
            people.remove(personName);
        else
            people.add(personName);
    }

    public String getPeopleString() {
        if (people.size() == 0)
            return "";

        String peopleString = people.get(0);
        for (int i = 1; i < people.size(); ++i)
            peopleString += ", " + people.get(i);

        return peopleString;
    }
}
