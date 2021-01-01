package com.example.lepaking_system.restaurant.model;

import java.io.Serializable;

public class MenuRestaurant implements Serializable {

    String id;
    String name;
    float price;
    String type;
    String email;

    public MenuRestaurant() {
    }

    public MenuRestaurant(String id, String name, float price, String type, String email) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.email = email;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
