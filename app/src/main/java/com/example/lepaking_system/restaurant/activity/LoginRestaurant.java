package com.example.lepaking_system.restaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.conversion.AESCrypt;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginRestaurant extends AppCompatActivity {

    //Variables for elements
    private Button signUpButton, loginButton;
    private ImageView restaurantLogo;
    private TextView welcomeText, signInText;
    private TextInputLayout restaurantEmail, restaurantPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_restaurant);

        //Hooks
        restaurantLogo = findViewById(R.id.restaurant_logo_login);
        welcomeText = findViewById(R.id.restaurant_welcome_text_login);
        signInText = findViewById(R.id.restaurant_sign_in_text_login);
        signUpButton = findViewById(R.id.restaurant_sign_up_button_login);
        loginButton = findViewById(R.id.restaurant_login_button_login);
        restaurantEmail = findViewById(R.id.restaurant_email_login);
        restaurantPassword = findViewById(R.id.restaurant_password_login);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRestaurant.this, SignUpRestaurant.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRestaurant(v);
            }
        });

    }

    private void loginRestaurant(View v) {
        //Validate Login Info
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        else{
            isRestaurant();
        }

    }

    private void isRestaurant() {

        final String restaurantEnteredEmail = Email.encodeEmail(restaurantEmail.getEditText().getText().toString().trim());
        final String restaurantEnteredPassword = restaurantPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Restaurant");

        Query checkRestaurant = reference.orderByChild("email").equalTo(restaurantEnteredEmail);

        checkRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                restaurantEmail.setError(null);
                restaurantEmail.setErrorEnabled(false);

                if(snapshot.exists()){
                    String passwordFromDB = snapshot.child(restaurantEnteredEmail).child("password").getValue(String.class);

                    try {
                        if(AESCrypt.decrypt(passwordFromDB).equals((restaurantEnteredPassword))){

                            restaurantPassword.setError(null);
                            restaurantPassword.setErrorEnabled(false);

                            String statefromDB = snapshot.child(restaurantEnteredEmail).child("state").getValue(String.class);
                            String menuPriceRangefromDB = snapshot.child(restaurantEnteredEmail).child("menuPriceRange").getValue(String.class);
                            String typefromDB = snapshot.child(restaurantEnteredEmail).child("type").getValue(String.class);

                            Intent intent = new Intent(getApplicationContext(), RestaurantMainActivity.class);
                            intent.putExtra("email", Email.decodeEmail(restaurantEnteredEmail));
                            intent.putExtra("state", statefromDB);
                            intent.putExtra("menuPriceRange", menuPriceRangefromDB);
                            intent.putExtra("type", typefromDB);
                            Toast.makeText(LoginRestaurant.this, "Log In Successfull!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();

                        }
                        else{
                            restaurantPassword.setError("Wrong Password");
                            restaurantPassword.requestFocus();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    private Boolean validatePassword() {
        String val = restaurantPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            restaurantPassword.setError("Field cannot be empty");
            return false;
        } else {
            restaurantPassword.setError(null);
            restaurantPassword.setErrorEnabled(false);
            return true;
        }
    }
}