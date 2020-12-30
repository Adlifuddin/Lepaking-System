package com.example.lepaking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProfileSetupActivity extends AppCompatActivity {

    private Button finishButton; //initialize button
    private EditText userName, userIc, userPhone, streetName, poscode, city; //initialize edittext
    private Spinner state;
    private String id; //initialize id
    private RadioGroup gender; //initialize radio group
    private RadioButton option; //initialize radio button

    DatabaseReference custDb; //initialize database reference to connect with firebase realtime database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        //initialize widgets in xml file
        finishButton = (Button) findViewById(R.id.finishButton);
        gender = findViewById(R.id.radioGroup);
        userName = (EditText) findViewById(R.id.userName);
        userIc = (EditText) findViewById(R.id.icNumber);
        userPhone = (EditText) findViewById(R.id.phoneNumber);
        streetName = (EditText) findViewById(R.id.streetName);
        poscode = (EditText) findViewById(R.id.poscodeNumber);
        city = (EditText) findViewById(R.id.cityName);
        state = (Spinner) findViewById(R.id.state);

        //when save button is clicked
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
                openMain();
            }
        });
    }

    //function to add student in firebase database
    private void addCustomer() {

        custDb = FirebaseDatabase.getInstance().getReference("Customer");

        int radioID = gender.getCheckedRadioButtonId(); //to save radio id
        option = (RadioButton) findViewById(radioID);

        String enteredName = (String) userName.getText().toString();
        String enteredIC = (String) userIc.getText().toString();
        String enteredMobile = (String) userPhone.getText().toString();
        String enteredGender = (String) option.getText().toString();
        String enteredStreetName = (String) streetName.getText().toString();
        String enteredPoscode = (String) poscode.getText().toString();
        String enteredCity = (String) city.getText().toString();
        String enteredState = (String) state.getSelectedItem().toString();

        //check the textfield
        if (!TextUtils.isEmpty(enteredName) && !TextUtils.isEmpty(enteredIC) && !TextUtils.isEmpty(enteredMobile) &&
                !TextUtils.isEmpty(enteredGender) && !TextUtils.isEmpty(enteredStreetName) && !TextUtils.isEmpty(enteredPoscode)
                && !TextUtils.isEmpty(enteredCity)  && !TextUtils.isEmpty(enteredState)) {

            //to get user id and email
            FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
            id = cust.getUid();

            //create student object
            CustomerInfo customer = new CustomerInfo(enteredName, enteredIC, enteredMobile, enteredGender, enteredStreetName,
                    enteredPoscode, enteredCity, enteredState);

            //save student in firebase
            custDb.child(id).setValue(customer);

            //display toast
            Toast.makeText(this, "Profile Complete", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Profile Incomplete", Toast.LENGTH_LONG).show();
        }
    }

    //function to change to main page
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
