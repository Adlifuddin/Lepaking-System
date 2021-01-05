package com.example.lepaking_system.restaurant.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.activity.RestaurantMainActivity;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.MenuRestaurant;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddMenuRestaurant extends Fragment {

    //Variables
    AutoCompleteTextView addRestaurantMenuTypeSelection;
    TextInputLayout addRestaurantMenuName, addRestaurantMenuPrice, addRestaurantMenuType;
    Button saveButton;

    String restaurant_email, restaurant_menuPriceRange, restaurant_type, restaurant_state;
    int restaurant_noOfMenu;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        restaurant_email = this.getArguments().getString("email");
        restaurant_state = this.getArguments().getString("state");
        restaurant_menuPriceRange = this.getArguments().getString("menuPriceRange");
        restaurant_type = this.getArguments().getString("type");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_menu_restaurant, container, false);

        //Hooks
        addRestaurantMenuTypeSelection = root.findViewById(R.id.add_restaurant_menu_type_selection);
        addRestaurantMenuName = root.findViewById(R.id.add_restaurant_menu_name);
        addRestaurantMenuPrice = root.findViewById(R.id.add_restaurant_menu_price);
        addRestaurantMenuType = root.findViewById(R.id.add_restaurant_menu_type);
        saveButton = root.findViewById(R.id.add_restaurant_menu_save_button);

        //Type option
        String[] menuTypeOption = {"Food", "Drink"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.option_for_restaurant, menuTypeOption);
        //Set default value
        addRestaurantMenuTypeSelection.setText(arrayAdapter.getItem(0).toString(), false);

        addRestaurantMenuTypeSelection.setAdapter(arrayAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewMenu();
            }
        });

        return root;

    }

    private void insertNewMenu() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference referenceMenu = rootNode.getReference("Menu");

        //Get all the values
        String menuID = referenceMenu.push().getKey();
        String menuName = addRestaurantMenuName.getEditText().getText().toString();
        String price = addRestaurantMenuPrice.getEditText().getText().toString();
        String type = addRestaurantMenuType.getEditText().getText().toString();

        MenuRestaurant addNewMenuRestaurant = new MenuRestaurant(menuID, menuName, Float.parseFloat(price), type, restaurant_email);
        referenceMenu.child(Email.encodeEmail(restaurant_email)).child(menuID).setValue(addNewMenuRestaurant);
        Toast.makeText(getContext(), "New menu is succesfully inserted!", Toast.LENGTH_SHORT).show();

        DatabaseReference referenceRestaurant = rootNode.getReference("Restaurant");
        Query incrementMenuCounter = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(restaurant_email));

        incrementMenuCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    restaurant_noOfMenu = snapshot.child(Email.encodeEmail(restaurant_email)).child("menuCounter").getValue(int.class);
                    restaurant_noOfMenu += 1;
                    referenceRestaurant.child(Email.encodeEmail(restaurant_email)).child("menuCounter").setValue(restaurant_noOfMenu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Delay 1 seconds before going to new activity to allow new activity refresh to get new data from firebase database using internet
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getActivity(), RestaurantMainActivity.class);
                intent.putExtra("email", restaurant_email);
                intent.putExtra("state", restaurant_state);
                intent.putExtra("menuPriceRange", restaurant_menuPriceRange);
                intent.putExtra("type", restaurant_type);
                startActivity(intent);
                getActivity().finish();
            }
        }, 2000);



    }

}

