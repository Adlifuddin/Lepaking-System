package com.example.lepaking_system.restaurant.model;

public class Restaurant {

    String id;
    String name, email, password, phoneNumber, streetName;
    int postcode;
    String city, state;
    int customerCounter = 0;
    int menuCounter = 0;
    String menuPriceRange;
    String type;
    int restaurantCapacity = 0;

    public Restaurant() {
    }

    public Restaurant(String id, String name, String email, String password, String phoneNumber, String streetName, int postcode, String city,
                      String state, int customerCounter, int menuCounter, String menuPriceRange, String type, int restaurantCapacity) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.streetName = streetName;
        this.postcode = postcode;
        this.city = city;
        this.state = state;
        this.customerCounter = customerCounter;
        this.menuCounter = menuCounter;
        this.menuPriceRange = menuPriceRange;
        this.type = type;
        this.restaurantCapacity = restaurantCapacity;
    }

    public int getRestaurantCapacity() {
        return restaurantCapacity;
    }

    public void setRestaurantCapacity(int restaurantCapacity) {
        this.restaurantCapacity = restaurantCapacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCustomerCounter() {
        return customerCounter;
    }

    public void setCustomerCounter(int customerCounter) {
        this.customerCounter = customerCounter;
    }

    public int getMenuCounter() {
        return menuCounter;
    }

    public void setMenuCounter(int menuCounter) {
        this.menuCounter = menuCounter;
    }

    public String getMenuPriceRange() {
        return menuPriceRange;
    }

    public void setMenuPriceRange(String menuPriceRange) {
        this.menuPriceRange = menuPriceRange;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
