package com.example.lepaking_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lepaking_system.restaurant.conversion.Email;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadRestaurantData extends AppCompatActivity {

    //Variables for elements
    DatabaseReference custDb;
    FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
    String id = cust.getUid();

    private Button loginButton;
    private TextInputLayout restaurantEmail;
    private List<Menu> restaurant = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        readCustomer();
    }

    private void readCustomer() {

        FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
        String id = cust.getUid();

        custDb = FirebaseDatabase.getInstance().getReference("Customer").child(id);
        custDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String currentRest = dataSnapshot.child(id).child("currentRest").getValue().toString();
                isRestaurant(currentRest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isRestaurant(String test) {

        final String restaurantEnteredEmail = Email.encodeEmail(test.toString().trim());

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

                    Intent intent = new Intent(getApplicationContext(), UserMenu.class);
                    intent.putExtra("email", restaurantEmail);
                    intent.putExtra("name", restaurantName);
                    intent.putExtra("streetName", restaurantStreet);
                    intent.putExtra("city", restaurantCity);
                    intent.putExtra("state", restaurantState);

                    startActivity(intent);

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

}
