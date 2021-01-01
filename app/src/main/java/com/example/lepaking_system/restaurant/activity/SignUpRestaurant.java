package com.example.lepaking_system.restaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.conversion.AESCrypt;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.Cheap;
import com.example.lepaking_system.restaurant.model.Chinese;
import com.example.lepaking_system.restaurant.model.Expensive;
import com.example.lepaking_system.restaurant.model.Indian;
import com.example.lepaking_system.restaurant.model.Malay;
import com.example.lepaking_system.restaurant.model.Moderate;
import com.example.lepaking_system.restaurant.model.Restaurant;
import com.example.lepaking_system.restaurant.model.Total;
import com.example.lepaking_system.restaurant.model.Western;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpRestaurant extends AppCompatActivity {

    //Variables for drop down list for State Selection
    private AutoCompleteTextView stateSelection, menuPriceRangeSelection, typeSelection;
    //Variables for login and sign up button
    private Button loginButton, signUpButton;
    //Variable for text input layout
    private TextInputLayout restaurantName, restaurantEmail, restaurantPassword, restaurantPhoneNumber, restaurantStreetName, restaurantPostcode, restaurantCity, restaurantState, restaurantMenuPriceRange, restaurantType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_restaurant);

        //Hooks
        loginButton = findViewById(R.id.restaurant_login_button_sign_up);
        signUpButton = findViewById(R.id.restaurant_sign_up_button_sign_up);
        restaurantEmail = findViewById(R.id.restaurant_email_sign_up);
        restaurantPassword = findViewById(R.id.restaurant_password_sign_up);
        restaurantName = findViewById(R.id.restaurant_name_sign_up);
        restaurantPhoneNumber = findViewById(R.id.restaurant_phone_number_sign_up);
        restaurantStreetName = findViewById(R.id.restaurant_street_name_sign_up);
        restaurantPostcode = findViewById(R.id.restaurant_postcode_sign_up);
        restaurantCity = findViewById(R.id.restaurant_city_sign_up);
        restaurantState = findViewById(R.id.restaurant_state_sign_up);
        restaurantMenuPriceRange = findViewById(R.id.restaurant_menu_price_range_sign_up);
        restaurantType = findViewById(R.id.restaurant_type_sign_up);
        stateSelection = findViewById(R.id.restaurant_select_state_sign_up);
        menuPriceRangeSelection = findViewById(R.id.restaurant_select_menu_price_range_sign_up);
        typeSelection = findViewById(R.id.restaurant_select_type_sign_up);

        //state option
        String[] stateOption = {"Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan",
                "Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah", "Sarawak",
                "Selangor", "Terengganu", "Wilayah Persekutuan Kuala Lumpur", "Wilayah Persekutuan Labuan", "Wilayah Persekutuan Putrajaya"};

        ArrayAdapter arrayStateAdapter = new ArrayAdapter(this, R.layout.option_for_restaurant, stateOption);
        //to make default value
        stateSelection.setText(arrayStateAdapter.getItem(0).toString(), false);

        stateSelection.setAdapter(arrayStateAdapter);

        //menu price range option
        String[] menuPriceRangeOption = {"Cheap", "Moderate", "Expensive"};

        ArrayAdapter arrayMenuPriceRangeAdapter = new ArrayAdapter(this, R.layout.option_for_restaurant, menuPriceRangeOption);
        //to make default value
        menuPriceRangeSelection.setText(arrayMenuPriceRangeAdapter.getItem(0).toString(), false);

        menuPriceRangeSelection.setAdapter(arrayMenuPriceRangeAdapter);

        //type range option
        String[] typeOption = {"Chinese", "Malay", "Indian", "Western"};

        ArrayAdapter arrayTypeAdapter = new ArrayAdapter(this, R.layout.option_for_restaurant, typeOption);
        //to make default value
        typeSelection.setText(arrayTypeAdapter.getItem(0).toString(), false);

        typeSelection.setAdapter(arrayTypeAdapter);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpRestaurant.this, LoginRestaurant.class);
                startActivity(intent);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewRestaurant();
            }
        });

    }

    private void insertNewRestaurant() {

        String email = restaurantEmail.getEditText().getText().toString();

        if (!validateName() | !validateEmail() | !validatePassword() | !validatePhoneNumber() | !validateStreetName() | !validatePoscode() | !validateCity()) {
            return;
        }


        //Variables for firebase database
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference referenceRestaurant = rootNode.getReference("Restaurant");
        DatabaseReference referenceRecommendationRestaurant = rootNode.getReference("recommendation_restaurant");

        Query isRestaurantEmailTaken = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(email));

        //check if entered email has been taken, if yes, ask user to reenter, if no, sign up successful.

        isRestaurantEmailTaken.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    restaurantEmail.setError("This email address has been taken");
                    return;
                } else {
                    //Get all the values

                    String id = referenceRestaurant.push().getKey();
                    String name = restaurantName.getEditText().getText().toString();
                    String email = Email.encodeEmail(restaurantEmail.getEditText().getText().toString());
                    String password = restaurantPassword.getEditText().getText().toString();
                    String phoneNumber = restaurantPhoneNumber.getEditText().getText().toString();
                    String streetName = restaurantStreetName.getEditText().getText().toString();
                    int postcode = Integer.parseInt(restaurantPostcode.getEditText().getText().toString());
                    String city = restaurantCity.getEditText().getText().toString();
                    String state = restaurantState.getEditText().getText().toString();
                    String menuPriceRange = restaurantMenuPriceRange.getEditText().getText().toString();
                    String type = restaurantType.getEditText().getText().toString();
                    int customerCounter = 0;
                    int menuCounter = 0;

                    String cheap = "P1";
                    String moderate = "P2";
                    String expensive = "P3";
                    String chinese = "T1";
                    String malay = "T2";
                    String indian = "T3";
                    String western = "T4";
                    String r = "R";
                    String total = "Total";

                    Cheap cheapRating = new Cheap(0);
                    Moderate moderateRating = new Moderate(0);
                    Expensive expensiveRating = new Expensive(0);
                    Chinese chineseRating = new Chinese(0);
                    Malay malayRating = new Malay(0);
                    Indian indianRating = new Indian(0);
                    Western westernRating = new Western(0);
                    com.example.lepaking_system.restaurant.model.R rValue = new com.example.lepaking_system.restaurant.model.R(0);
                    Total totalValue = new Total(0);

                    referenceRecommendationRestaurant.child(email).child(cheap).setValue(cheapRating);
                    referenceRecommendationRestaurant.child(email).child(moderate).setValue(moderateRating);
                    referenceRecommendationRestaurant.child(email).child(expensive).setValue(expensiveRating);
                    referenceRecommendationRestaurant.child(email).child(chinese).setValue(chineseRating);
                    referenceRecommendationRestaurant.child(email).child(malay).setValue(malayRating);
                    referenceRecommendationRestaurant.child(email).child(indian).setValue(indianRating);
                    referenceRecommendationRestaurant.child(email).child(western).setValue(westernRating);
                    referenceRecommendationRestaurant.child(email).child(r).setValue(rValue);
                    referenceRecommendationRestaurant.child(email).child(total).setValue(totalValue);

                    Restaurant addNewRestaurant = new Restaurant();
                    try {
                        addNewRestaurant = new Restaurant(id, name, email, AESCrypt.encrypt(password), phoneNumber, streetName, postcode, city, state, customerCounter, menuCounter, menuPriceRange, type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    referenceRestaurant.child(email).setValue(addNewRestaurant);

                    System.out.println(menuPriceRange);

                    if(menuPriceRange.equals("Cheap")){
                        cheapRating.setRating(1);
                        referenceRecommendationRestaurant.child(email).child(cheap).setValue(cheapRating);
                    }
                    else if(menuPriceRange.equals("Moderate")){
                        moderateRating.setRating(1);
                        referenceRecommendationRestaurant.child(email).child(moderate).setValue(moderateRating);
                    }
                    else if(menuPriceRange.equals("Expensive")){
                        expensiveRating.setRating(1);
                        referenceRecommendationRestaurant.child(email).child(expensive).setValue(expensiveRating);
                    }

                    if(type.equals("Chinese")){
                        chineseRating.setRating(1);
                        referenceRecommendationRestaurant.child(email).child(chinese).setValue(chineseRating);
                    }
                    else if(type.equals("Malay")){
                        malayRating.setRating(1);
                        referenceRecommendationRestaurant.child(email).child(malay).setValue(malayRating);
                    }
                    else if(type.equals("Indian")){
                        indianRating.setRating(1);
                        referenceRecommendationRestaurant.child(email).child(indian).setValue(indianRating);
                    }
                    else if(type.equals("Western")){
                        westernRating.setRating(1);
                        referenceRecommendationRestaurant.child(email).child(western).setValue(westernRating);
                    }

                    restaurantEmail.setError(null);
                    restaurantEmail.setErrorEnabled(false);

                    Toast.makeText(SignUpRestaurant.this, "Sign Up Successfull!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpRestaurant.this, LoginRestaurant.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private Boolean validateName() {
        String val = restaurantName.getEditText().getText().toString();

        if (val.isEmpty()) {
            restaurantName.setError("Field cannot be empty");
            return false;
        } else {
            restaurantName.setError(null);
            restaurantName.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateEmail() {
        String val = restaurantEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (val.isEmpty()) {
            restaurantEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            restaurantEmail.setError("Invalid email address");
            return false;
        } else {
            restaurantEmail.setError(null);
            restaurantEmail.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validatePassword() {
        String val = restaurantPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            restaurantPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            restaurantPassword.setError("Password is too weak");
            return false;
        } else {
            restaurantPassword.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String val = restaurantPhoneNumber.getEditText().getText().toString();

        if (val.isEmpty()) {
            restaurantPhoneNumber.setError("Field cannot be empty");
            return false;
        } else {
            restaurantPhoneNumber.setError(null);
            return true;
        }
    }

    private Boolean validateStreetName() {
        String val = restaurantStreetName.getEditText().getText().toString();

        if (val.isEmpty()) {
            restaurantStreetName.setError("Field cannot be empty");
            return false;
        } else {
            restaurantStreetName.setError(null);
            return true;
        }
    }

    private Boolean validatePoscode() {
        String val = restaurantPostcode.getEditText().getText().toString();

        if (val.isEmpty()) {
            restaurantPostcode.setError("Field cannot be empty");
            return false;
        } else {
            restaurantPostcode.setError(null);
            return true;
        }

    }

    private Boolean validateCity() {
        String val = restaurantCity.getEditText().getText().toString();

        if (val.isEmpty()) {
            restaurantCity.setError("Field cannot be empty");
            return false;
        } else {
            restaurantCity.setError(null);
            return true;
        }
    }

}