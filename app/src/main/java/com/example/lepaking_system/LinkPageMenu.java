package com.example.lepaking_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.ui.main.DisplayRestaurantInfo;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LinkPageMenu extends AppCompatActivity {

    //Variables for elements
    private Button loginButton;
    private TextInputLayout restaurantEmail;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Menu> restaurant = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hooks
        loginButton = findViewById(R.id.restaurant_login_button_login);
        restaurantEmail = findViewById(R.id.restaurant_email_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginRestaurant(v);
            }
        });

    }

    private void loginRestaurant(View v) {

        //Validate Login Info
        if (!validateEmail()) {
            return;
        }
        else{
            isRestaurant();
        }

    }

    private void isRestaurant() {

        final String restaurantEnteredEmail = Email.encodeEmail(restaurantEmail.getEditText().getText().toString().trim());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Restaurants");

        Query checkRestaurant = reference.orderByChild("email").equalTo(restaurantEnteredEmail);

        checkRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                restaurantEmail.setError(null);
                restaurantEmail.setErrorEnabled(false);

                if(snapshot.exists()){

                    //First database
                    String restaurantEmail = snapshot.child(restaurantEnteredEmail).child("email").getValue(String.class);
                    String restaurantName = snapshot.child(restaurantEnteredEmail).child("name").getValue(String.class);
                    String restaurantStreet = snapshot.child(restaurantEnteredEmail).child("streetName").getValue(String.class);
                    String restaurantCity = snapshot.child(restaurantEnteredEmail).child("city").getValue(String.class);
                    String restaurantState = snapshot.child(restaurantEnteredEmail).child("state").getValue(String.class);

                    Intent intent = new Intent(getApplicationContext(), DisplayRestaurantInfo.class);
                    intent.putExtra("email", restaurantEmail);
                    intent.putExtra("name", restaurantName);
                    intent.putExtra("streetName", restaurantStreet);
                    intent.putExtra("city", restaurantCity);
                    intent.putExtra("state", restaurantState);

                    startActivity(intent);
//                    ReadData addNewRestaurant = new ReadData();
//
//                    try {
//                        addNewRestaurant = new ReadData(restaurantName, restaurantStreet, restaurantCity, restaurantState);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    Toast.makeText(LinkPageMenu.this, "Log In Successfull!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    restaurantEmail.setError("No such email exist");
                    restaurantEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Boolean validateEmail(){
        String val = restaurantEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            restaurantEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            restaurantEmail.setError("Invalid email address");
            return false;
        }
        else{
            restaurantEmail.setError(null);
            restaurantEmail.setErrorEnabled(false);
            return true;
        }
    }
}