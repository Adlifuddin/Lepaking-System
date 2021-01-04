package com.example.lepaking_system;

public class CustomerInfo {

    private String name;
    private String IC;
    private String mobileNo;
    private String gender;
    private String streetName;
    private String poscode;
    private String city;
    private String state;
    private String currentRest;

    //default constructor
    public CustomerInfo(){
    }

    //alternate constructor
    public CustomerInfo(String name, String IC, String mobileNo, String gender, String streetName, String poscode, String city, String state, String currentRest) {
        this.name = name;
        this.IC = IC;
        this.mobileNo = mobileNo;
        this.gender = gender;
        this.streetName = streetName;
        this.poscode = poscode;
        this.city = city;
        this.state = state;
        this.currentRest = currentRest;
    }

    //getter
    public String getName() {
        return name;
    }

    public String getIC() {
        return IC;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getPoscode() {
        return poscode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCurrentRest() {
        return currentRest;
    }
}
