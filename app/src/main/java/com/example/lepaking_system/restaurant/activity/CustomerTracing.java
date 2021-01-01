package com.example.lepaking_system.restaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.adapter.CustomerTracingAdapter;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.Validation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerTracing extends AppCompatActivity {

    List<Validation> validationList;
    RecyclerView recyclerView;
    TextView totalCustomer;
    CustomerTracingAdapter customerTracingAdapter;
    DatabaseReference reference;
    String restaurant_email;
    String customer_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_tracing);

        //Hooks
        recyclerView = findViewById(R.id.customer_tracing_recycler_view);
        totalCustomer = findViewById(R.id.customer_tracing_total_customer);

        Intent intent = getIntent();
        restaurant_email = intent.getStringExtra("email");
        customer_date = intent.getStringExtra("date");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        validationList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Customer_Tracing").child(Email.encodeEmail(restaurant_email)).child(customer_date);
        System.out.println(restaurant_email);
        Query checkCustomerTracing = reference.orderByChild("id");

        checkCustomerTracing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Validation validation = ds.getValue(Validation.class);
                        validationList.add(validation);
                    }
                    customerTracingAdapter = new CustomerTracingAdapter(validationList);
                    recyclerView.setAdapter(customerTracingAdapter);
                    totalCustomer.setText("Total Customers: " + snapshot.getChildrenCount());
                }
                else{
                    totalCustomer.setText("Total Customers: " + 0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**@Override
    public void onBackPressed() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
    }
     **/
}