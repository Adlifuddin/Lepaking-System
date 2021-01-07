package com.example.lepaking_system.restaurant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.activity.CaptureAct;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

public class ValidatePayment extends Fragment implements View.OnClickListener {

    Button scanQRCodeButton;
    Button minusCustomerButton;
    TextInputLayout minusCustomer;

    String restaurant_email;
    int restaurant_noOfCustomer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        restaurant_email = this.getArguments().getString("email");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_validate_payment, container, false);

        //Hooks
        scanQRCodeButton = root.findViewById(R.id.validate_payment_scan_qr_code_button);
        minusCustomerButton = root.findViewById(R.id.validate_payment_minus_customer_button);
        minusCustomer = root.findViewById(R.id.validate_payment_minus_customer);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference referenceRestaurant = rootNode.getReference("Restaurant");
        Query incrementCustomerCounter = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(restaurant_email));

        incrementCustomerCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    restaurant_noOfCustomer = snapshot.child(Email.encodeEmail(restaurant_email)).child("customerCounter").getValue(int.class);
                    if(restaurant_noOfCustomer == 0){
                        minusCustomerButton.setEnabled(false);
                    }
                    minusCustomerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int value = Integer.parseInt(minusCustomer.getEditText().getText().toString());
                            restaurant_noOfCustomer -= value;
                            referenceRestaurant.child(Email.encodeEmail(restaurant_email)).child("customerCounter").setValue(restaurant_noOfCustomer);
                            if(restaurant_noOfCustomer == 0){
                                minusCustomerButton.setEnabled(false);
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        scanQRCodeButton.setOnClickListener(this);

        return root;
    }

    private void minusCustomer() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference referenceRestaurant = rootNode.getReference("Restaurant");
        Query incrementCustomerCounter = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(restaurant_email));

        incrementCustomerCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    restaurant_noOfCustomer = snapshot.child(Email.encodeEmail(restaurant_email)).child("customerCounter").getValue(int.class);
                    if(restaurant_noOfCustomer == 0){
                        minusCustomerButton.setEnabled(false);
                    }
                    int value = Integer.parseInt(minusCustomer.getEditText().getText().toString());
                    restaurant_noOfCustomer -= value;
                    referenceRestaurant.child(Email.encodeEmail(restaurant_email)).child("customerCounter").setValue(restaurant_noOfCustomer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        scanQRCode();
    }

    public void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(ValidatePayment.this.getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        ValidatePayment.this.getActivity().startActivityForResult(integrator.createScanIntent(), 2);
    }
}
