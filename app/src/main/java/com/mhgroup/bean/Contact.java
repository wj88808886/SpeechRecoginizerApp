package com.mhgroup.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DK Wang on 2015/3/20.
 */
public class Contact {

    private String name;
    private List<String> phoneNumber;

    public Contact()
    {
        name = new String();
        phoneNumber = new ArrayList<String>();
    }

    public Contact(String name, List<String> phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void addPhoneNumber(String phoneNumber)
    {
        this.phoneNumber.add(phoneNumber);
    }

}
