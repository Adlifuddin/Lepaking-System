package com.example.lepaking_system.restaurant.conversion;

public class Email {
    public static String encodeEmail(String email) {
        return email.replace(".", ",");
    }

    public static String decodeEmail(String email) {
        return email.replace(",", ".");
    }
}
