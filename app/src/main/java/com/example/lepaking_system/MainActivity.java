package com.example.lepaking_system;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.lepaking_system.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private String id; //initialize id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to get user unique id from signin or signup activity
        FirebaseUser customer = FirebaseAuth.getInstance().getCurrentUser();
        id = customer.getUid();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_check);
        tabs.getTabAt(1).setIcon(R.drawable.ic_search);
        tabs.getTabAt(2).setIcon(R.drawable.ic_restaurant_menu);
        tabs.getTabAt(3).setIcon(R.drawable.ic_payment);
        tabs.getTabAt(4).setIcon(R.drawable.ic_account_circle);

        int position = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("viewpager_position");
        }
        //select which tab to open first
        tabs.getTabAt(position).select();
    }
}
