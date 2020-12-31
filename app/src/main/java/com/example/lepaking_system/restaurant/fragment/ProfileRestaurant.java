package com.example.lepaking_system.restaurant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class ProfileRestaurant extends Fragment {

    //Variables for elements
    TextInputLayout phoneNumber, streetName, poscode, city, state, password, menuPriceRange, type;
    TextView name, email, id, noOfCustomer, noOfMenu;

    String restaurant_email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile_restaurant, container, false);

        restaurant_email = this.getArguments().getString("email");

        //Hooks
        name = root.findViewById(R.id.restaurant_name_profile);
        email = root.findViewById(R.id.restaurant_email_profile);
        id = root.findViewById(R.id.restaurant_id_profile);
        noOfCustomer = root.findViewById(R.id.restaurant_no_of_customer_profile);
        noOfMenu = root.findViewById(R.id.restaurant_no_of_menu_profile);
        phoneNumber = root.findViewById(R.id.restaurant_phone_number_profile);
        streetName = root.findViewById(R.id.restaurant_street_name_profile);
        poscode = root.findViewById(R.id.restaurant_poscode_profile);
        city = root.findViewById(R.id.restaurant_city_profile);
        state = root.findViewById(R.id.restaurant_state_profile);
        password = root.findViewById(R.id.restaurant_password_profile);
        menuPriceRange = root.findViewById(R.id.restaurant_menu_price_range_profile);
        type = root.findViewById(R.id.restaurant_type_profile);

        showAllRestaurantData();

        return root;
    }

    private void showAllRestaurantData() {

        String encodedRestaurantEmail = Email.encodeEmail(restaurant_email);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Restaurant");

        Query restaurantData = reference.orderByChild("email").equalTo(encodedRestaurantEmail);

        restaurantData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String emailFromDB = Email.decodeEmail(snapshot.child(encodedRestaurantEmail).child("email").getValue(String.class));
                    String passwordFromDB = snapshot.child(encodedRestaurantEmail).child("password").getValue(String.class);
                    String nameFromDB = snapshot.child(encodedRestaurantEmail).child("name").getValue(String.class);
                    String phoneNumberFromDB = snapshot.child(encodedRestaurantEmail).child("phoneNumber").getValue(String.class);
                    String streetNameFromDB = snapshot.child(encodedRestaurantEmail).child("streetName").getValue(String.class);
                    int poscodeFromDB = snapshot.child(encodedRestaurantEmail).child("postcode").getValue(int.class);
                    String cityFromDB = snapshot.child(encodedRestaurantEmail).child("city").getValue(String.class);
                    String stateFromDB = snapshot.child(encodedRestaurantEmail).child("state").getValue(String.class);
                    int customerCounterFromDB = snapshot.child(encodedRestaurantEmail).child("customerCounter").getValue(int.class);
                    int menuCounterFromDB = snapshot.child(encodedRestaurantEmail).child("menuCounter").getValue(int.class);
                    String menuPriceRangeFromDB = snapshot.child(encodedRestaurantEmail).child("menuPriceRange").getValue(String.class);
                    String typeFromDB = snapshot.child(encodedRestaurantEmail).child("type").getValue(String.class);
                    String idFromDB = snapshot.child(encodedRestaurantEmail).child("id").getValue(String.class);

                    name.setText(nameFromDB);
                    email.setText(emailFromDB);
                    id.setText(idFromDB);
                    noOfCustomer.setText("" + customerCounterFromDB);
                    noOfMenu.setText("" + menuCounterFromDB);
                    phoneNumber.getEditText().setText(phoneNumberFromDB);
                    streetName.getEditText().setText(streetNameFromDB);
                    poscode.getEditText().setText("" + poscodeFromDB);
                    city.getEditText().setText(cityFromDB);
                    state.getEditText().setText(stateFromDB);
                    try {
                        password.getEditText().setText(AESCrypt.decrypt(passwordFromDB));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    menuPriceRange.getEditText().setText(menuPriceRangeFromDB);
                    type.getEditText().setText(typeFromDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static ProfileRestaurant newInstance(String email) {
        ProfileRestaurant profileRestaurant = new ProfileRestaurant();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        profileRestaurant.setArguments(bundle);
        return profileRestaurant;
    }
}

