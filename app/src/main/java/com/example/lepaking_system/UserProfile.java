package com.example.lepaking_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends Fragment {

    private TextView outName1;
    private EditText outName2, outEmail, outIC, outPhone, outStreet, outPoscode, outCity; //initialize edit text
    private Spinner outGender, outState;
    private Button editButton, settingButton, logoutButton; //initialize button
    private Boolean status = true;

    DatabaseReference custDb; //initialize database reference

    FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();

    public UserProfile(){
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_userprofile, container, false);

        //initialize widgets in xml file
        outName1 = v.findViewById(R.id.outputName1);
        outName2 = v.findViewById(R.id.outputName2);
        outEmail = v.findViewById(R.id.outputEmail);
        outIC = v.findViewById(R.id.outputIC);
        outGender = v.findViewById(R.id.outputGender);
        outPhone = v.findViewById(R.id.outputPhoneNo);
        outStreet = v.findViewById(R.id.outputStreet);
        outPoscode = v.findViewById(R.id.outputPoscode);
        outCity = v.findViewById(R.id.outputCity);
        outState = v.findViewById(R.id.outputState);

        //get id
        String id = cust.getUid();

        //initialize database path
        custDb = FirebaseDatabase.getInstance().getReference("Customer").child(id);
        custDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String ic = dataSnapshot.child("ic").getValue().toString();
                String mobileNo = dataSnapshot.child("mobileNo").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();

                String email = cust.getEmail();

                String streetName = dataSnapshot.child("streetName").getValue().toString();
                String poscode = dataSnapshot.child("poscode").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();
                String state = dataSnapshot.child("state").getValue().toString();

                outName1.setText(name);

                outName2.setText(name);
                outName2.setFocusable(false);

                outEmail.setText(email);
                outEmail.setFocusable(false);

                outIC.setText(ic);
                outIC.setFocusable(false);

                outPhone.setText(mobileNo);
                outPhone.setFocusable(false);

                outStreet.setText(streetName);
                outStreet.setFocusable(false);

                outPoscode.setText(poscode);
                outPoscode.setFocusable(false);

                outCity.setText(city);
                outCity.setFocusable(false);

                if(gender.equals("male")){
                    outGender.setSelection(0);
                }
                else if(gender.equals("female")){
                    outGender.setSelection(1);
                }

                outGender.setEnabled(false);

                if(state.equals("Pulau Pinang")){
                    outState.setSelection(0);
                }
                else if(state.equals("Melaka")){
                    outState.setSelection(1);
                }
                else if(state.equals("Sabah")){
                    outState.setSelection(2);
                }
                else if(state.equals("Sarawak")){
                    outState.setSelection(3);
                }
                else if(state.equals("Johor")){
                    outState.setSelection(4);
                }
                else if(state.equals("Kedah")){
                    outState.setSelection(5);
                }
                else if(state.equals("Kelantan")){
                    outState.setSelection(6);
                }
                else if(state.equals("Negeri Sembilan")){
                    outState.setSelection(7);
                }
                else if(state.equals("Pahang")){
                    outState.setSelection(8);
                }
                else if(state.equals("Perak")){
                    outState.setSelection(9);
                }
                else if(state.equals("Perlis")){
                    outState.setSelection(10);
                }
                else if(state.equals("Selangor")){
                    outState.setSelection(11);
                }
                else if(state.equals("Terengganu")){
                    outState.setSelection(12);
                }
                else if(state.equals("Kuala Lumpur")){
                    outState.setSelection(13);
                }
                else if(state.equals("Labuan")){
                    outState.setSelection(14);
                }
                else if(state.equals("Putrajaya")){
                    outState.setSelection(15);
                }

                outState.setEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //setting button
        settingButton = v.findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openUpdate();
            }
        });

        //setting button
        logoutButton = v.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //edit button
        editButton = v.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(status){
                    editButton.setText("Save");

                    outName2.setFocusableInTouchMode(true);
                    outEmail.setFocusableInTouchMode(true);
                    outIC.setFocusableInTouchMode(true);
                    outGender.setEnabled(true);
                    outPhone.setFocusableInTouchMode(true);
                    outStreet.setFocusableInTouchMode(true);
                    outPoscode.setFocusableInTouchMode(true);
                    outCity.setFocusableInTouchMode(true);
                    outState.setEnabled(true);

                    status=false;
                } else {
                    editButton.setText("Edit Profile");

                    String enteredName = (String) outName2.getText().toString();
                    String enteredIC = (String) outIC.getText().toString();
                    String enteredMobile = (String) outPhone.getText().toString();
                    String enteredGender = (String) outGender.getSelectedItem().toString();
                    String enteredStreetName = (String) outStreet.getText().toString();
                    String enteredPoscode = (String) outPoscode.getText().toString();
                    String enteredCity = (String) outCity.getText().toString();
                    String enteredState = (String) outState.getSelectedItem().toString();

                    //email need edit from auth
                    String enteredEmail = (String) outEmail.getText().toString();

                    //initialize database path
                    custDb.child("name").setValue(enteredName);
                    custDb.child("ic").setValue(enteredIC);
                    custDb.child("mobileNo").setValue(enteredMobile);
                    custDb.child("gender").setValue(enteredGender);

                    cust.updateEmail(enteredEmail);

                    //custDb.child("email").setValue(enteredEmail);

                    custDb.child("streetName").setValue(enteredStreetName);
                    custDb.child("poscode").setValue(enteredPoscode);
                    custDb.child("city").setValue(enteredCity);
                    custDb.child("state").setValue(enteredState);

                    outName2.setFocusable(false);
                    outEmail.setFocusable(false);
                    outIC.setFocusable(false);
                    outGender.setEnabled(false);
                    outPhone.setFocusable(false);
                    outStreet.setFocusable(false);
                    outPoscode.setFocusable(false);
                    outCity.setFocusable(false);
                    outState.setEnabled(false);

                    status=true;
                }

            }
        });

        return v;
    }

    //function to change to main page
    public void openUpdate(){
        Intent intent = new Intent(getActivity(), UserUpdateActivity.class);
        startActivity(intent);
    }

    //function to change to main page
    public void logout(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }
}
