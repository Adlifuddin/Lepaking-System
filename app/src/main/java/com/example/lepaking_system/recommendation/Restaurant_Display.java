package com.example.lepaking_system.recommendation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lepaking_system.Menu;
import com.example.lepaking_system.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Restaurant_Display extends AppCompatActivity {

    private ImageButton mSearchBtn;
    private RecyclerView foodList, drinkList;
    private DatabaseReference mRestDatabase;
    private TextView r_name,r_price,r_type,r_address,r_phone,r_rating,r_customer,r_rating_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danial_display_search);

        mRestDatabase = FirebaseDatabase.getInstance().getReference("Menu");

        r_name = findViewById(R.id.r_name);
        r_price = findViewById(R.id.r_price);
        r_type = findViewById(R.id.r_type);
        r_address = findViewById(R.id.r_address);
        r_phone = findViewById(R.id.r_phone);
        r_rating = findViewById(R.id.r_rating);
        r_customer = findViewById(R.id.r_customer);
        r_rating_customer = findViewById(R.id.r_rating_num);

        //mSearchField = (EditText) findViewById(R.id.search_button);
//        mSearchBtn = (ImageButton) findViewById(R.id.back_btn);

        foodList = (RecyclerView) findViewById(R.id.food_list);
        foodList.setHasFixedSize(true);
        foodList.setLayoutManager(new LinearLayoutManager(this));


        drinkList = (RecyclerView) findViewById(R.id.drink_list);
        drinkList.setHasFixedSize(true);
        drinkList.setLayoutManager(new LinearLayoutManager(this));

        String Restaurant_ID = "test@yahoo,com";

        Intent intent = getIntent();
        Restaurant_ID = String.valueOf(intent.getStringExtra("the product key"));

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
        String finalRestaurant_ID = Restaurant_ID;
        reff.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                r_name.setText(String.valueOf(snapshot.child("Restaurant").child(finalRestaurant_ID).child("name").getValue()));
                r_price.setText(String.valueOf(snapshot.child("Restaurant").child(finalRestaurant_ID).child("menuPriceRange").getValue()));
                r_type.setText(String.valueOf(snapshot.child("Restaurant").child(finalRestaurant_ID).child("type").getValue()));
                r_address.setText(String.valueOf(snapshot.child("Restaurant").child(finalRestaurant_ID).child("streetName").getValue()));
                r_phone.setText(String.valueOf(snapshot.child("Restaurant").child(finalRestaurant_ID).child("phoneNumber").getValue()));
                r_customer.setText(String.valueOf(snapshot.child("Restaurant").child(finalRestaurant_ID).child("customerCounter").getValue()));
                r_rating.setText(String.valueOf(snapshot.child("recommendation_restaurant").child(finalRestaurant_ID).child("R").child("value").getValue()));
                String rating_cus = "(" + String.valueOf(snapshot.child("recommendation_restaurant").child(finalRestaurant_ID).child("Total").child("value").getValue()) + ")";
                r_rating_customer.setText(rating_cus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //must get the key
        firebasesearch(Restaurant_ID);


    }

    private void firebasesearch(String productkey) {

        //drink
        Query firebaseSearchQuery = mRestDatabase.child(productkey).orderByChild("type").startAt("Drink").endAt("Drink" + "\uf8ff");
        FirebaseRecyclerAdapter<Menu, Restaurant_Display.RestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Menu, Restaurant_Display.RestViewHolder>(
                Menu.class,
                R.layout.danial_food_drinks,
                Restaurant_Display.RestViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(Restaurant_Display.RestViewHolder restViewHolder, Menu menu, int i) {
                restViewHolder.setDetails(getApplicationContext(), menu.getName(), menu.getPrice());
            }
        };

        //food
        Query firebaseSearchQuery1 = mRestDatabase.child(productkey).orderByChild("type").startAt("Food").endAt("Food" + "\uf8ff");
        FirebaseRecyclerAdapter<Menu, Restaurant_Display.RestViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Menu, Restaurant_Display.RestViewHolder>(
                Menu.class,
                R.layout.danial_food_drinks,
                Restaurant_Display.RestViewHolder.class,
                firebaseSearchQuery1
        ) {
            @Override
            protected void populateViewHolder(Restaurant_Display.RestViewHolder restViewHolder, Menu menu, int i) {
                restViewHolder.setDetails(getApplicationContext(), menu.getName(), menu.getPrice());
            }
        };

        drinkList.setAdapter(firebaseRecyclerAdapter);
        foodList.setAdapter(firebaseRecyclerAdapter1);
    }

    public static class RestViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public RestViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDetails(Context ctx, String name, float price){

            TextView restau_name = (TextView) mView.findViewById(R.id.danial_food_txt);
            TextView restau_type = (TextView) mView.findViewById(R.id.danial_price_txt);

            restau_name.setText(name);
            restau_type.setText(String.valueOf(price));
        }

    }

}
