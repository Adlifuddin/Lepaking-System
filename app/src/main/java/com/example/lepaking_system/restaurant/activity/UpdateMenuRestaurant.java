package com.example.lepaking_system.restaurant.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.MenuRestaurant;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateMenuRestaurant extends AppCompatActivity {

    //Variables
    AutoCompleteTextView updateRestaurantMenuTypeSelection;
    TextView updateRestaurantMenuID;
    TextInputLayout updateRestaurantMenuName, updateRestaurantMenuPrice, updateRestaurantMenuType;

    int position;

    DatabaseReference referenceMenu;

    String menuEmail, menuName, menuType, menuID;
    Float menuPrice;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu_restaurant);

        //to get object which is the chosen update menu
        MenuRestaurant menu = (MenuRestaurant) getIntent().getExtras().getSerializable("RESTAURANTMENU");
        menuEmail = menu.getEmail();

        referenceMenu = FirebaseDatabase.getInstance().getReference("Menu").child(Email.encodeEmail(menuEmail));


        //Hooks
        updateRestaurantMenuTypeSelection = findViewById(R.id.update_restaurant_menu_type_selection);
        updateRestaurantMenuName = findViewById(R.id.update_restaurant_menu_name);
        updateRestaurantMenuPrice = findViewById(R.id.update_restaurant_menu_price);
        updateRestaurantMenuType = findViewById(R.id.update_restaurant_menu_type);
        updateRestaurantMenuID = findViewById(R.id.update_restaurant_menu_id);

        menuName = menu.getName();
        menuPrice = menu.getPrice();
        menuType = menu.getType();
        menuID = menu.getId();

        updateRestaurantMenuName.getEditText().setText(menuName);
        updateRestaurantMenuPrice.getEditText().setText(String.valueOf(menuPrice));
        updateRestaurantMenuType.getEditText().setText(menuType);
        updateRestaurantMenuID.setText(menuID);

        //Type option
        String[] menuTypeOption = {"Food", "Drink"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.option_for_restaurant, menuTypeOption);

        if(menuType.equals("Food")){
            position = 0;
        }
        else if(menuType.equals("Drink")){
            position = 1;
        }
        //Set default value
        updateRestaurantMenuTypeSelection.setText(arrayAdapter.getItem(position).toString(), false);

        updateRestaurantMenuTypeSelection.setAdapter(arrayAdapter);


    }

    public void Update(View view) {
        if(isMenuNameChanged() | isMenuPriceChanged() | isMenuTypeChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else{
            Toast.makeText(this, "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMenuTypeChanged() {
        if(!menuType.equals(updateRestaurantMenuType.getEditText().getText().toString())){

            referenceMenu.child(menuID).child("type").setValue(updateRestaurantMenuType.getEditText().getText().toString());
            menuType = updateRestaurantMenuType.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isMenuPriceChanged() {
        if (!String.valueOf(menuPrice).equals(updateRestaurantMenuPrice.getEditText().getText().toString()))
        {
            referenceMenu.child(menuID).child("price").setValue(Float.parseFloat(updateRestaurantMenuPrice.getEditText().getText().toString()));
            menuPrice = Float.parseFloat(updateRestaurantMenuPrice.getEditText().getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isMenuNameChanged() {
        if(!menuName.equals(updateRestaurantMenuName.getEditText().getText().toString())){

            referenceMenu.child(menuID).child("name").setValue(updateRestaurantMenuName.getEditText().getText().toString());
            menuName = updateRestaurantMenuName.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }
}
