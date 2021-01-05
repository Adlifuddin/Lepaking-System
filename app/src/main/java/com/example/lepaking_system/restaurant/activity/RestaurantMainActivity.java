package com.example.lepaking_system.restaurant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.fragment.AddMenuRestaurant;
import com.example.lepaking_system.restaurant.fragment.ProfileRestaurant;
import com.example.lepaking_system.restaurant.fragment.ShowMenuRestaurant;
import com.example.lepaking_system.restaurant.fragment.UpdateProfileRestaurant;
import com.example.lepaking_system.restaurant.fragment.ValidateCustomer;
import com.example.lepaking_system.restaurant.fragment.ValidatePayment;
import com.example.lepaking_system.restaurant.model.Validation;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RestaurantMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener{


    private DrawerLayout drawer;

    private Dialog dialog;

    private String restaurant_email, restaurant_menuPriceRange, restaurant_type, restaurant_state;
    private int restaurant_noOfCustomer;

    private String globalEmail, globalMenuPriceRange, globalType,  globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Hooks
        drawer = findViewById(R.id.drawer_layout);

        dialog = new Dialog(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        //Handle the handburger menu item and its animation when it is open and close
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    getRestaurantProfileData()).commit();

            navigationView.setCheckedItem(R.id.nav_my_profile);
        }

        TextView nav_header = headerView.findViewById(R.id.restaurant_email_nav_header);
        nav_header.setText(globalEmail);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        getRestaurantProfileData()).commit();
                break;

            case R.id.nav_update_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        passRestaurantProfileData()).commit();
                break;

            case R.id.nav_show_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        passRestaurantProfileEmail_2()).commit();
                break;

            case R.id.nav_add_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        passRestaurantProfileEmail_1()).commit();
                break;

            case R.id.nav_validate_customer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        passRestaurantProfileEmail_3()).commit();
                break;
            case R.id.nav_validate_payment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        passRestaurantProfileEmail_4()).commit();
                break;
            case R.id.nav_customer_tracing:
                showDatePickerDialog();
                break;
            case R.id.nav_log_out:
                Intent intent = new Intent(getApplicationContext(), LoginRestaurant.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Successfully Log Out", Toast.LENGTH_SHORT).show();
                break;
        }

        //Close our drawer after clicking chosen fragment
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        //close drawer when back button is pressed
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String day ="";
        String formatMonth ="";
        if(dayOfMonth == 1){
            day = "01";
        }
        else if(dayOfMonth == 2){
            day = "02";
        }
        else if(dayOfMonth == 3){
            day = "03";
        }
        else if(dayOfMonth == 4){
            day = "04";
        }
        else if(dayOfMonth == 5){
            day = "05";
        }
        else if(dayOfMonth == 6){
            day = "06";
        }
        else if(dayOfMonth == 7){
            day = "07";
        }
        else if(dayOfMonth == 8){
            day = "08";
        }
        else if(dayOfMonth == 9){
            day = "09";
        }

        if(month == 0){
            formatMonth = "01";
        }
        else if(month == 1){
            formatMonth = "02";
        }
        else if(month == 2){
            formatMonth = "03";
        }
        else if(month == 3){
            formatMonth = "04";
        }
        else if(month == 4){
            formatMonth = "05";
        }
        else if(month == 5){
            formatMonth = "06";
        }
        else if(month == 6){
            formatMonth = "07";
        }
        else if(month == 7){
            formatMonth = "08";
        }
        else if(month == 8){
            formatMonth = "09";
        }
        else if(month == 9){
            formatMonth = "10";
        }
        else if(month == 10){
            formatMonth = "11";
        }
        else if(month == 11){
            formatMonth = "12";
        }


        String date = day + "-" + formatMonth + "-" + year;
        System.out.println(date);

        Intent intent = new Intent(this, CustomerTracing.class);
        intent.putExtra("email", globalEmail);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    private ProfileRestaurant getRestaurantProfileData() {
        Bundle extras = getIntent().getExtras();
        restaurant_email = extras.getString("email");
        restaurant_state = extras.getString("state");
        restaurant_menuPriceRange = extras.getString("menuPriceRange");
        restaurant_type = extras.getString("type");

        globalEmail = restaurant_email;
        globalState = restaurant_state;
        globalMenuPriceRange = restaurant_menuPriceRange;
        globalType = restaurant_type;

        ProfileRestaurant profileRestaurant = ProfileRestaurant.newInstance(restaurant_email);
        return profileRestaurant;
    }

    private UpdateProfileRestaurant passRestaurantProfileData() {

        Bundle bundle = new Bundle();
        bundle.putString("email", globalEmail);
        bundle.putString("state", globalState);
        bundle.putString("menuPriceRange", globalMenuPriceRange);
        bundle.putString("type", globalType);
        System.out.println("globalType" + globalType);

        UpdateProfileRestaurant updateProfileRestaurant = new UpdateProfileRestaurant();
        updateProfileRestaurant.setArguments(bundle);
        return updateProfileRestaurant;
    }

    private AddMenuRestaurant passRestaurantProfileEmail_1() {

        Bundle bundle = new Bundle();
        bundle.putString("email", globalEmail);
        bundle.putString("state", globalState);
        bundle.putString("menuPriceRange", globalMenuPriceRange);
        bundle.putString("type", globalType);

        AddMenuRestaurant addMenuRestaurant = new AddMenuRestaurant();
        addMenuRestaurant.setArguments(bundle);
        return addMenuRestaurant;
    }

    private ShowMenuRestaurant passRestaurantProfileEmail_2() {

        Bundle bundle = new Bundle();
        bundle.putString("email", globalEmail);

        ShowMenuRestaurant showMenuRestaurant = new ShowMenuRestaurant();
        showMenuRestaurant.setArguments(bundle);
        return showMenuRestaurant;
    }

    private ValidateCustomer passRestaurantProfileEmail_3() {

        Bundle bundle = new Bundle();
        bundle.putString("email", globalEmail);

        ValidateCustomer validateCustomer = new ValidateCustomer();
        validateCustomer.setArguments(bundle);
        return validateCustomer;
    }

    private ValidatePayment passRestaurantProfileEmail_4() {

        Bundle bundle = new Bundle();
        bundle.putString("email", globalEmail);

        ValidatePayment validatePayment = new ValidatePayment();
        validatePayment.setArguments(bundle);
        return validatePayment;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
        if (result != null) {

            if (result.getContents() != null) {

                if(requestCode == 1 || requestCode == 2){

                    String customerData[] = result.getContents().split(";");
                    String customer_id = customerData[0];
                    String customer_email = customerData[1];
                    String customer_name = customerData[2];
                    String customer_phone_number = customerData[3];
                    String customer_street_name = customerData[4];
                    int customer_poscode = Integer.parseInt(customerData[5]);
                    String customer_city = customerData[6];
                    String customer_state = customerData[7];
                    System.out.println("customer_id: " + customer_id);
                    System.out.println("customer_email: " + customer_email);
                    System.out.println("customer_name: " + customer_name);
                    System.out.println("customer_phone_number: " + customer_phone_number);
                    System.out.println("customer_street_name : " + customer_street_name);
                    System.out.println("customer_poscode : " + customer_poscode);
                    System.out.println("customer_city : " + customer_city);
                    System.out.println("customer_state : " + customer_state);

                    if (requestCode == 1) {
                        //qr code from validate customer
                        ShowValidateCustomerDialog(customer_id, customer_email, customer_name, customer_phone_number, customer_street_name, customer_poscode, customer_city, customer_state);
                    } else if (requestCode == 2) {
                        //qr code from validate payment
                        ShowValidatePaymentDialog(customer_id, customer_email, customer_name, customer_phone_number, customer_street_name, customer_poscode, customer_city, customer_state);
                    }
                }
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ShowValidatePaymentDialog(String customer_id, String customer_email, String customer_name, String customer_phone_number, String customer_street_name, int customer_poscode, String customer_city, String customer_state) {

        TextInputLayout email, name, phone_number, street_name, poscode, city, state;
        TextView date, time;
        Button done_button;

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

        dialog.setContentView(R.layout.validate_payment_pop_up);

        email = dialog.findViewById(R.id.payment_email);
        name = dialog.findViewById(R.id.payment_name);
        phone_number = dialog.findViewById(R.id.payment_phone_number);
        street_name = dialog.findViewById(R.id.payment_street_name);
        poscode = dialog.findViewById(R.id.payment_poscode);
        city = dialog.findViewById(R.id.payment_city);
        state = dialog.findViewById(R.id.payment_state);
        date = dialog.findViewById(R.id.validate_payment_date);
        time = dialog.findViewById(R.id.validate_payment_time);
        done_button = dialog.findViewById(R.id.validate_payment_done_scan_button);

        //get current date
        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String checkOutDate = format.format(todaysdate);

        //get current time
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat current_date = new SimpleDateFormat("HH:mm");
        // you can get seconds by adding  "...:ss" to it
        current_date.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String checkOutTime = current_date.format(currentLocalTime);

        DatabaseReference referenceCustomerTracing = rootNode.getReference("Customer_Tracing").child(Email.encodeEmail(globalEmail)).child(checkOutDate);
        Query checkCustomerTracing = referenceCustomerTracing.orderByChild("id").equalTo(customer_id);

        checkCustomerTracing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    email.getEditText().setText(customer_email);
                    name.getEditText().setText(customer_name);
                    phone_number.getEditText().setText(customer_phone_number);
                    street_name.getEditText().setText(customer_street_name);
                    poscode.getEditText().setText("" + customer_poscode);
                    city.getEditText().setText(customer_city);
                    state.getEditText().setText(customer_state);
                    date.setText(checkOutDate);
                    time.setText(checkOutTime);

                    referenceCustomerTracing.child(customer_id).child("checkOutDate").setValue(checkOutDate);
                    referenceCustomerTracing.child(customer_id).child("checkOutTime").setValue(checkOutTime);

                    DatabaseReference referenceRestaurant = rootNode.getReference("Restaurant");
                    Query decrementCustomerCounter = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(globalEmail));

                    decrementCustomerCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                restaurant_noOfCustomer = snapshot.child(Email.encodeEmail(globalEmail)).child("customerCounter").getValue(int.class);
                                restaurant_noOfCustomer -= 1;
                                referenceRestaurant.child(Email.encodeEmail(globalEmail)).child("customerCounter").setValue(restaurant_noOfCustomer);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dialog.show();
                }
                else{
                    Toast.makeText(RestaurantMainActivity.this, "Invalid QR Code", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference referenceCurrentRestaurant = FirebaseDatabase.getInstance().getReference("Customer").child(customer_id);
        referenceCurrentRestaurant.child("currentRest").setValue("null");

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void ShowValidateCustomerDialog(String customer_id, String customer_email, String customer_name, String customer_phone_number, String customer_street_name, int customer_poscode, String customer_city, String customer_state) {
        TextInputLayout email, name, phone_number, street_name, poscode, city, state;
        TextView date, time;
        Button done_button;

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

        dialog.setContentView(R.layout.validate_customer_pop_up);

        //Hooks
        email = dialog.findViewById(R.id.customer_email);
        name = dialog.findViewById(R.id.customer_name);
        phone_number = dialog.findViewById(R.id.customer_phone_number);
        street_name = dialog.findViewById(R.id.customer_street_name);
        poscode = dialog.findViewById(R.id.customer_poscode);
        city = dialog.findViewById(R.id.customer_city);
        state = dialog.findViewById(R.id.customer_state);
        date = dialog.findViewById(R.id.validate_customer_date);
        time = dialog.findViewById(R.id.validate_customer_time);
        done_button = dialog.findViewById(R.id.done_scan_button);

        email.getEditText().setText(customer_email);
        name.getEditText().setText(customer_name);
        phone_number.getEditText().setText(customer_phone_number);
        street_name.getEditText().setText(customer_street_name);
        poscode.getEditText().setText("" + customer_poscode);
        city.getEditText().setText(customer_city);
        state.getEditText().setText(customer_state);

        //get current date
        String checkInDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        date.setText(checkInDate);

        //get current time and date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat current_date = new SimpleDateFormat("HH:mm");
        String checkInTime = current_date.format(currentLocalTime);
        time.setText(checkInTime);

        String checkOutDate = "-";
        String checkOutTime = "-";

        DatabaseReference referenceCustomer = rootNode.getReference("Customer_Tracing");
        Validation addNewValidationTracing = new Validation(globalEmail, customer_id, Email.encodeEmail(customer_email), customer_name, customer_phone_number, checkInDate, checkInTime, checkOutDate, checkOutTime);
        referenceCustomer.child(Email.encodeEmail(globalEmail)).child(checkInDate).child(customer_id).setValue(addNewValidationTracing);

        DatabaseReference referenceRestaurant = rootNode.getReference("Restaurant");
        Query incrementCustomerCounter = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(globalEmail));

        incrementCustomerCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    restaurant_noOfCustomer = snapshot.child(Email.encodeEmail(globalEmail)).child("customerCounter").getValue(int.class);
                    restaurant_noOfCustomer += 1;
                    referenceRestaurant.child(Email.encodeEmail(globalEmail)).child("customerCounter").setValue(restaurant_noOfCustomer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference referenceCurrentRestaurant = FirebaseDatabase.getInstance().getReference("Customer").child(customer_id);
        referenceCurrentRestaurant.child("currentRest").setValue(globalEmail);



        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}