package com.example.lepaking_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lepaking_system.recommendation.Recommendation_Insert;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.MenuRestaurant;
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

public class UserMenu extends Fragment {

    private Button addcart, dropcart;
    private TextView nameofRestaurant, streetName, stateName, totalprice, orderAmount;

    List<MenuRestaurant> foodList;
    List<MenuRestaurant> drinkList;
    RecyclerView recyclerView, recyclerView2;
    TextView totalMenu;
    DisplayMenuDetails foodAdapter, drinkAdapter;
    DatabaseReference reference;
    DatabaseReference custDb;
    FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
    String id = cust.getUid();

    //recommendation purposes
    RatingBar ratingBar;
    Button rate_at_menu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        //initialize widgets in xml file
        nameofRestaurant = v.findViewById(R.id.restaurantName);
        streetName = v.findViewById(R.id.street);
        stateName = v.findViewById(R.id.state);
        totalprice = v.findViewById(R.id.restaurant_show_menu_total_menu);
        recyclerView = v.findViewById(R.id.restaurant_show_food_recycler_view);
        recyclerView2 = v.findViewById(R.id.restaurant_show_drink_recycler_view);
        addcart = v.findViewById(R.id.restaurant_show_menu_update_button);
        
        dropcart = v.findViewById(R.id.drop_cart);
        orderAmount = v.findViewById(R.id.restaurant_amount_menu);

        //rating purposes
        ratingBar = v.findViewById(R.id.ratingBar);
        rate_at_menu = v.findViewById(R.id.rate_button_menu);
        Recommendation_Insert recommendation_insert = new Recommendation_Insert();



        FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
        String id = cust.getUid();

        custDb = FirebaseDatabase.getInstance().getReference("Customer").child(id);
        custDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String currentRest = String.valueOf(dataSnapshot.child("currentRest").getValue().toString());
                final String restaurantEnteredEmail = Email.encodeEmail(currentRest);

                if(currentRest.equals("null")){
                    ratingBar.setEnabled(false);
                    rate_at_menu.setEnabled(false);
                }

                //for rating purposes
                rate_at_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int currentrate = (int) ratingBar.getRating();

                        if(currentrate == 0){
                            System.out.println("cannot be 0=============================");
                            Toast.makeText(getContext(), "Rating cannot be 0 star", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            recommendation_insert.update_rating(restaurantEnteredEmail,id,currentrate);
                            ratingBar.setEnabled(false);
                            rate_at_menu.setEnabled(false);
                        }
                    }
                });

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Restaurant");
                Query checkRestaurant = reference.orderByChild("email").equalTo(restaurantEnteredEmail);
                checkRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            String restaurantName = snapshot.child(restaurantEnteredEmail).child("name").getValue(String.class);
                            String restaurantStreet = snapshot.child(restaurantEnteredEmail).child("streetName").getValue(String.class);
                            String restaurantCity = snapshot.child(restaurantEnteredEmail).child("city").getValue(String.class);
                            String restaurantState = snapshot.child(restaurantEnteredEmail).child("state").getValue(String.class);

                            nameofRestaurant.setText(restaurantName);
                            streetName.setText("Location : " + restaurantStreet + ", " + restaurantCity);
                            stateName.setText(restaurantState);



                        }
                        else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

                foodList = new ArrayList<>();
                drinkList = new ArrayList<>();;

                reference = FirebaseDatabase.getInstance().getReference("Menu").child(Email.encodeEmail(currentRest));;

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            MenuRestaurant menuList = ds.getValue(MenuRestaurant.class);
                            String typeFood = menuList.getType();
                            if(typeFood.equals("Food")){
                                foodList.add(menuList);
                            }
                            else if(typeFood.equals("Drink")){
                                System.out.println(menuList.getName());
                                drinkList.add(menuList);
                            }
                        }
                        foodAdapter = new DisplayMenuDetails(foodList);
                        recyclerView.setAdapter(foodAdapter);
                        drinkAdapter = new DisplayMenuDetails(drinkList);
                        recyclerView2.setAdapter(drinkAdapter);
                        totalprice.setText("Total Menu(s): "+ snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

}
