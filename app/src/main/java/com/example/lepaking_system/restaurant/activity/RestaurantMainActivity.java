package com.example.lepaking_system.restaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.fragment.ProfileRestaurant;
import com.google.android.material.navigation.NavigationView;

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
             /**
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
              **/
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
}