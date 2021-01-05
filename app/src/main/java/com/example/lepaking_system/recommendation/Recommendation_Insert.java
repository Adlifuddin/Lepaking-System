package com.example.lepaking_system.recommendation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lepaking_system.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Recommendation_Insert extends AppCompatActivity {
    String resID, usID;
    int rating;
    DatabaseReference reff1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_ui);

        EditText restaurant_id, user_id, rate_id;
        Button rating_btn;
        restaurant_id = (EditText) findViewById(R.id.restaurant_text_id);
        user_id = (EditText) findViewById(R.id.user_text_id);
        rate_id = (EditText) findViewById(R.id.rating_text_id);
        rating_btn = (Button) findViewById(R.id.rate_btn);

        rating_btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) { //take from database and put into our data
                resID = restaurant_id.getText().toString().trim();
                usID = user_id.getText().toString().trim();
                rating = Integer.parseInt(rate_id.getText().toString());
                update_rating(resID, usID, rating);
            }
        }));

    }

    //PASS RESTAURANT YANG DIA RATE AND USER YANG RATE WITH RATING r
    public void update_rating(String restaurantID,String userID, int r){

        // nak cari user rating in db based from the new one
        final Restaurantdata restaurantdata = new Restaurantdata();
        final Userdata userdata = new Userdata();


        //set user db to user object
        reff1 = FirebaseDatabase.getInstance().getReference();
        reff1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float newrating; //for the new rating
                int newtotal; //for the new total customer rated the restaurant
                String Ptype, Ttype;

                //retrieve from USER data db
                float P1 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P1").child("rating").getValue()));
                float P2 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P2").child("rating").getValue()));
                float P3 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P3").child("rating").getValue()));
                float T1 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T1").child("rating").getValue()));
                float T2 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T2").child("rating").getValue()));
                float T3 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T3").child("rating").getValue()));
                float T4 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T4").child("rating").getValue()));
                int P1total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P1").child("restaurant_number").getValue()));
                int P2total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P2").child("restaurant_number").getValue()));
                int P3total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P3").child("restaurant_number").getValue()));
                int T1total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T1").child("restaurant_number").getValue()));
                int T2total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T2").child("restaurant_number").getValue()));
                int T3total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T3").child("restaurant_number").getValue()));
                int T4total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T4").child("restaurant_number").getValue()));

                userdata.setP1(P1);
                userdata.setP2(P2);
                userdata.setP3(P3);
                userdata.setT1(T1);
                userdata.setT2(T2);
                userdata.setT3(T3);
                userdata.setT4(T4);

                userdata.setP1total(P1total);
                userdata.setP2total(P2total);
                userdata.setP3total(P3total);
                userdata.setT1total(T1total);
                userdata.setT2total(T2total);
                userdata.setT3total(T3total);
                userdata.setT4total(T4total);

                //retrieve from RESTAURANT data db
                int Pp1 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("P1").child("rating").getValue()));
                int Pp2 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("P2").child("rating").getValue()));
                int Pp3 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("P3").child("rating").getValue()));
                int Tp1 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T1").child("rating").getValue()));
                int Tp2 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T2").child("rating").getValue()));
                int Tp3 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T3").child("rating").getValue()));
                int Tp4 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T4").child("rating").getValue()));
                int total = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("Total").child("value").getValue()));
                float rating = Float.parseFloat(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("R").child("value").getValue()));

                restaurantdata.setP1(Pp1);
                restaurantdata.setP2(Pp2);
                restaurantdata.setP3(Pp3);
                restaurantdata.setT1(Tp1);
                restaurantdata.setT2(Tp2);
                restaurantdata.setT3(Tp3);
                restaurantdata.setT4(Tp4);
                restaurantdata.setTotal(total);
                restaurantdata.setRating(rating);


                if(restaurantdata.P1 == 1){
                    newrating = ((userdata.P1*userdata.P1total) + r) / (userdata.P1total + 1);
                    newtotal = userdata.P1total + 1;
                    userdata.setP1total(newtotal);
                    userdata.setP1(newrating);
                    Ptype = "P1";
                }
                else if(restaurantdata.P2 == 1){
                    newrating = ((userdata.P2*userdata.P2total) + r) / (userdata.P2total + 1);
                    newtotal = userdata.P2total + 1;
                    userdata.setP2total(newtotal);
                    userdata.setP2(newrating);
                    Ptype = "P2";
                }
                else {
                    newrating = ((userdata.P3*userdata.P3total) + r) / (userdata.P3total + 1);
                    newtotal = userdata.P3total + 1;
                    userdata.setP3total(newtotal);
                    userdata.setP3(newrating);
                    Ptype = "P3";
                }

                //updating in database
                reff1.child("Recommendation_Customer").child(userID).child(Ptype).child("rating").setValue(newrating);
                reff1.child("Recommendation_Customer").child(userID).child(Ptype).child("restaurant_number").setValue(newtotal);

                if(restaurantdata.T1 == 1){
                    newrating = ((userdata.T1*userdata.T1total) + r) / (userdata.T1total + 1);
                    newtotal = userdata.T1total + 1;
                    userdata.setT1total(newtotal);
                    userdata.setT1(newrating);
                    Ttype = "T1";
                }
                else if(restaurantdata.T2 == 1){
                    newrating = ((userdata.T2*userdata.T2total) + r) / (userdata.T2total + 1);
                    newtotal = userdata.T2total + 1;
                    userdata.setT2total(newtotal);
                    userdata.setT2(newrating);
                    Ttype = "T2";
                }
                else if(restaurantdata.T3 == 1){
                    newrating = ((userdata.T3*userdata.T3total) + r) / (userdata.T3total + 1);
                    newtotal = userdata.T3total + 1;
                    userdata.setT3total(newtotal);
                    userdata.setT3(newrating);
                    Ttype = "T3";
                }
                else {
                    newrating = ((userdata.T4*userdata.T4total) + r) / (userdata.T4total + 1);
                    newtotal = userdata.T4total + 1;
                    userdata.setT4total(newtotal);
                    userdata.setT4(newrating);
                    Ttype = "T4";
                }

                //update user T value to db
                reff1.child("Recommendation_Customer").child(userID).child(Ttype).child("rating").setValue(newrating);
                reff1.child("Recommendation_Customer").child(userID).child(Ttype).child("restaurant_number").setValue(newtotal);

                //CALCULATE RESTAURANT RATING
                float newRestaurantRating;
                int newRestaurantTotal;
                newRestaurantRating = ((restaurantdata.rating * restaurantdata.total) + r) / (restaurantdata.total + 1);
                newRestaurantTotal = restaurantdata.total + 1;
                restaurantdata.setRating(newRestaurantRating);
                restaurantdata.setTotal(newRestaurantTotal);

                //update restaurant rating to database
                reff1.child("recommendation_restaurant").child(restaurantID).child("R").child("value").setValue(newRestaurantRating);
                reff1.child("recommendation_restaurant").child(restaurantID).child("Total").child("value").setValue(newRestaurantTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
