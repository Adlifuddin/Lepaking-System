package com.example.lepaking_system.restaurant.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.lepaking_system.restaurant.conversion.AESCrypt;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.Cheap;
import com.example.lepaking_system.restaurant.model.Chinese;
import com.example.lepaking_system.restaurant.model.Expensive;
import com.example.lepaking_system.restaurant.model.Indian;
import com.example.lepaking_system.restaurant.model.Malay;
import com.example.lepaking_system.restaurant.model.Moderate;
import com.example.lepaking_system.restaurant.model.Western;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileRestaurant extends Fragment {

    //Variables for elements
    TextInputLayout restaurantID, restaurantName, restaurantEmail, restaurantPassword, restaurantPhoneNumber, restaurantStreetName, restaurantPostcode, restaurantCity, restaurantState, restaurantMenuPriceRange, restaurantType;;
    //Variables for drop down list for state Selection
    AutoCompleteTextView stateSelection, menuPriceRangeSelection, typeSelection;;
    //Variable for Button
    Button updateButton;
    //variables for item in drop down list for state selection
    int position, position1, position2;

    String restaurant_email, restaurant_name, restaurant_password, restaurant_phoneNumber,
            restaurant_streetName, restaurant_city, restaurant_state, restaurant_id, restaurant_menuPriceRange, restaurant_type;
    int restaurant_postcode;

    DatabaseReference referenceRestaurant, referenceRecommendationRestaurant;

    String encodedRestaurantEmail;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        referenceRestaurant = FirebaseDatabase.getInstance().getReference("Restaurant");
        referenceRecommendationRestaurant = FirebaseDatabase.getInstance().getReference("recommendation_restaurant");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_update_profile_restaurant, container, false);

        restaurant_email = this.getArguments().getString("email");
        restaurant_state = this.getArguments().getString("state");
        restaurant_menuPriceRange = this.getArguments().getString("menuPriceRange");
        restaurant_type = this.getArguments().getString("type");

        encodedRestaurantEmail = Email.encodeEmail(restaurant_email);

        //hooks
        updateButton = root.findViewById(R.id.restaurant_update_button_update_profile);
        restaurantName = root.findViewById(R.id.restaurant_name_update_profile);
        restaurantEmail = root.findViewById(R.id.restaurant_email_update_profile);
        restaurantPassword = root.findViewById(R.id.restaurant_password_update_profile);
        restaurantPhoneNumber = root.findViewById(R.id.restaurant_phone_number_update_profile);
        restaurantStreetName = root.findViewById(R.id.restaurant_street_name_update_profile);
        restaurantPostcode = root.findViewById(R.id.restaurant_poscode_update_profile);
        restaurantCity = root.findViewById(R.id.restaurant_city_update_profile);
        restaurantState = root.findViewById(R.id.restaurant_state_update_profile);
        stateSelection = root.findViewById(R.id.restaurant_select_state_update_profile);
        restaurantID = root.findViewById(R.id.restaurant_id_update_profile);
        restaurantMenuPriceRange = root.findViewById(R.id.restaurant_menu_price_range_update_profile);
        restaurantType = root.findViewById(R.id.restaurant_type_update_profile);
        menuPriceRangeSelection = root.findViewById(R.id.restaurant_select_menu_price_range_update_profile);
        typeSelection = root.findViewById(R.id.restaurant_select_type_update_profile);


        showRestaurantDataToBeUpdated();

        //state option
        String[] stateOption = {"Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan",
                "Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah", "Sarawak",
                "Selangor", "Terengganu", "Wilayah Persekutuan Kuala Lumpur", "Wilayah Persekutuan Labuan", "Wilayah Persekutuan Putrajaya"};

        ArrayAdapter arrayStateAdapter = new ArrayAdapter(getContext(), R.layout.option_for_restaurant, stateOption);

        //to make default value
        if(restaurant_state.equals("Johor")){
            position = 0;
        }
        else if(restaurant_state.equals("Kedah")){
            position = 1;
        }
        else if(restaurant_state.equals("Kelantan")){
            position = 2;
        }
        else if(restaurant_state.equals("Melaka")){
            position = 3;
        }
        else if(restaurant_state.equals("Negeri Sembilan")){
            position = 4;
        }
        else if(restaurant_state.equals("Pahang")){
            position = 5;
        }
        else if(restaurant_state.equals("Perak")){
            position = 6;
        }
        else if(restaurant_state.equals("Perlis")){
            position = 7;
        }
        else if(restaurant_state.equals("Pulau Pinang")){
            position = 8;
        }
        else if(restaurant_state.equals("Sabah")){
            position = 9;
        }
        else if(restaurant_state.equals("Sarawak")){
            position = 10;
        }
        else if(restaurant_state.equals("Selangor")){
            position = 11;
        }
        else if(restaurant_state.equals("Terengganu")){
            position = 12;
        }
        else if(restaurant_state.equals("Wilayah Persekutuan Kuala Lumpur")){
            position = 13;
        }
        else if(restaurant_state.equals("Wilayah Persekutuan Labuan")){
            position = 14;
        }
        else if(restaurant_state.equals("Wilayah Persekutuan Putrajaya")){
            position = 15;
        }

        stateSelection.setText(arrayStateAdapter.getItem(position).toString(), false);

        stateSelection.setAdapter(arrayStateAdapter);

        //menu price range option
        String[] menuPriceRangeOption = {"Cheap", "Moderate", "Expensive"};

        ArrayAdapter arrayMenuPriceRangeAdapter = new ArrayAdapter(getContext(), R.layout.option_for_restaurant, menuPriceRangeOption);

        //to make default value
        if(restaurant_menuPriceRange.equals("Cheap")){
            position1 = 0;
        }
        else if(restaurant_menuPriceRange.equals("Moderate")){
            position1 = 1;
        }
        else if(restaurant_menuPriceRange.equals("Expensive")){
            position1 = 2;
        }
        //to make default value
        menuPriceRangeSelection.setText(arrayMenuPriceRangeAdapter.getItem(position1).toString(), false);

        menuPriceRangeSelection.setAdapter(arrayMenuPriceRangeAdapter);

        //type range option
        String[] typeOption = {"Chinese", "Malay", "Indian", "Western"};

        ArrayAdapter arrayTypeAdapter = new ArrayAdapter(getContext(), R.layout.option_for_restaurant, typeOption);

        System.out.println("restaurant_type" + restaurant_type);

        //to make default value
        if(restaurant_type.equals("Cheap")){
            position2 = 0;
        }
        else if(restaurant_type.equals("Indian")){
            position2 = 1;
        }
        else if(restaurant_type.equals("Malay")){
            position2 = 2;
        }
        else if(restaurant_type.equals("Western")){
            position2 = 3;
        }

        //to make default value
        typeSelection.setText(arrayTypeAdapter.getItem(position2).toString(), false);

        typeSelection.setAdapter(arrayTypeAdapter);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        return root;
    }

    private void update() {
        if(isNameChanged()  |isPhoneNumberChanged() | isStreetNameChanged() | isPoscodeChanged() | isCityChanged() | isStateChanged() | isMenuPriceRangeChanged() | isTypeChanged() | isPasswordChanged()){
            Toast.makeText(getContext(), "Data has been successfully updated!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), RestaurantMainActivity.class);
            intent.putExtra("email", restaurant_email);
            intent.putExtra("state", restaurant_state);
            intent.putExtra("menuPriceRange", restaurant_menuPriceRange);
            intent.putExtra("type", restaurant_type);
            startActivity(intent);
            getActivity().finish();
        }
        else {
            Toast.makeText(getContext(), "Data is same and cannot be updated!", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isNameChanged() {
        if(!restaurant_name.equals(restaurantName.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("name").setValue(restaurantName.getEditText().getText().toString());
            restaurant_name = restaurantName.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isPhoneNumberChanged() {

        if(!restaurant_phoneNumber.equals(restaurantPhoneNumber.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("phoneNumber").setValue(restaurantPhoneNumber.getEditText().getText().toString());
            restaurant_phoneNumber = restaurantPhoneNumber.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isStreetNameChanged() {

        if(!restaurant_streetName.equals(restaurantStreetName.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("streetName").setValue(restaurantStreetName.getEditText().getText().toString());
            restaurant_streetName = restaurantStreetName.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isPoscodeChanged() {
        if(restaurant_postcode!=Integer.parseInt(restaurantPostcode.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("postcode").setValue(Integer.parseInt(restaurantPostcode.getEditText().getText().toString()));
            restaurant_postcode = Integer.parseInt(restaurantPostcode.getEditText().getText().toString());
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isCityChanged() {
        if(!restaurant_city.equals(restaurantCity.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("city").setValue(restaurantCity.getEditText().getText().toString());
            restaurant_city = restaurantCity.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isStateChanged() {
        if(!restaurant_state.equals(restaurantState.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("state").setValue(restaurantState.getEditText().getText().toString());
            restaurant_state = restaurantState.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isMenuPriceRangeChanged() {
        String cheap = "P1";
        String moderate = "P2";
        String expensive = "P3";

        if(!restaurant_menuPriceRange.equals(restaurantMenuPriceRange.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("menuPriceRange").setValue(restaurantMenuPriceRange.getEditText().getText().toString());

            Cheap cheapRating = new Cheap(0);
            Moderate moderateRating = new Moderate(0);
            Expensive expensiveRating = new Expensive(0);

            referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(cheap).setValue(cheapRating);
            referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(moderate).setValue(moderateRating);
            referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(expensive).setValue(expensiveRating);

            if(restaurantMenuPriceRange.getEditText().getText().toString().equals("Cheap")){
                cheapRating.setRating(1);
                referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(cheap).setValue(cheapRating);
            }
            else if(restaurantMenuPriceRange.getEditText().getText().toString().equals("Moderate")){
                moderateRating.setRating(1);
                referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(moderate).setValue(moderateRating);
            }
            else if(restaurantMenuPriceRange.getEditText().getText().toString().equals("Expensive")){
                expensiveRating.setRating(1);
                referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(expensive).setValue(expensiveRating);
            }

            restaurant_menuPriceRange = restaurantMenuPriceRange.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isTypeChanged() {
        String chinese = "T1";
        String malay = "T2";
        String indian = "T3";
        String western = "T4";


        if(!restaurant_type.equals(restaurantType.getEditText().getText().toString())){

            referenceRestaurant.child(encodedRestaurantEmail).child("type").setValue(restaurantType.getEditText().getText().toString());
            Chinese chineseRating = new Chinese(0);
            Malay malayRating = new Malay(0);
            Indian indianRating = new Indian(0);
            Western westernRating = new Western(0);

            referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(chinese).setValue(chineseRating);
            referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(malay).setValue(malayRating);
            referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(indian).setValue(indianRating);
            referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(western).setValue(westernRating);


            if(restaurantType.getEditText().getText().toString().equals("Chinese")){
                chineseRating.setRating(1);
                referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(chinese).setValue(chineseRating);
            }
            else if(restaurantType.getEditText().getText().toString().equals("Malay")){
                malayRating.setRating(1);
                referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(malay).setValue(malayRating);
            }
            else if(restaurantType.getEditText().getText().toString().equals("Indian")){
                indianRating.setRating(1);
                referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(indian).setValue(indianRating);
            }
            else if(restaurantType.getEditText().getText().toString().equals("Western")){
                westernRating.setRating(1);
                referenceRecommendationRestaurant.child(encodedRestaurantEmail).child(western).setValue(westernRating);
            }

            restaurant_type = restaurantType.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isPasswordChanged() {
        if(!restaurant_password.equals(restaurantPassword.getEditText().getText().toString())){

            try {
                referenceRestaurant.child(encodedRestaurantEmail).child("password").setValue(AESCrypt.encrypt(restaurantPassword.getEditText().getText().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            restaurant_password = restaurantPassword.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private void showRestaurantDataToBeUpdated() {

        Query restaurantData = referenceRestaurant.orderByChild("email").equalTo(encodedRestaurantEmail);

        restaurantData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    restaurant_email = Email.decodeEmail(snapshot.child(encodedRestaurantEmail).child("email").getValue(String.class));
                    restaurant_password = snapshot.child(encodedRestaurantEmail).child("password").getValue(String.class);
                    restaurant_name = snapshot.child(encodedRestaurantEmail).child("name").getValue(String.class);
                    restaurant_phoneNumber = snapshot.child(encodedRestaurantEmail).child("phoneNumber").getValue(String.class);
                    restaurant_streetName = snapshot.child(encodedRestaurantEmail).child("streetName").getValue(String.class);
                    restaurant_postcode = snapshot.child(encodedRestaurantEmail).child("postcode").getValue(int.class);
                    restaurant_city = snapshot.child(encodedRestaurantEmail).child("city").getValue(String.class);
                    restaurant_id = snapshot.child(encodedRestaurantEmail).child("id").getValue(String.class);
                    restaurant_menuPriceRange = snapshot.child(encodedRestaurantEmail).child("menuPriceRange").getValue(String.class);
                    restaurant_type = snapshot.child(encodedRestaurantEmail).child("type").getValue(String.class);

                    restaurantName.getEditText().setText(restaurant_name);
                    restaurantEmail.getEditText().setText(restaurant_email);
                    restaurantID.getEditText().setText(restaurant_id);
                    restaurantPhoneNumber.getEditText().setText(restaurant_phoneNumber);
                    restaurantStreetName.getEditText().setText(restaurant_streetName);
                    restaurantPostcode.getEditText().setText("" + restaurant_postcode);
                    restaurantCity.getEditText().setText(restaurant_city);
                    try {
                        restaurantPassword.getEditText().setText(AESCrypt.decrypt(restaurant_password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

