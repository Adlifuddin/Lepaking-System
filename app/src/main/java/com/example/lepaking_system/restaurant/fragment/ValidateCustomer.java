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

public class ValidateCustomer extends Fragment implements View.OnClickListener{

    Button scanQRCodeButton;
    Button addCustomerButton;

    String restaurant_email;
    TextInputLayout addCustomer;
    int restaurant_noOfCustomer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        restaurant_email = this.getArguments().getString("email");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_validate_customer, container, false);

        //Hooks
        scanQRCodeButton = root.findViewById(R.id.validate_customer_scan_qr_code_button);
        addCustomerButton = root.findViewById(R.id.validate_customer_add_customer_button);
        addCustomer = root.findViewById(R.id.validate_customer_add_customer);

        scanQRCodeButton.setOnClickListener(this);

        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
            }
        });

        return root;
    }

    private void addCustomer() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference referenceRestaurant = rootNode.getReference("Restaurant");
        Query incrementCustomerCounter = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(restaurant_email));

        incrementCustomerCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    restaurant_noOfCustomer = snapshot.child(Email.encodeEmail(restaurant_email)).child("customerCounter").getValue(int.class);
                    int value = Integer.parseInt(addCustomer.getEditText().getText().toString());
                    restaurant_noOfCustomer += value;
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
        IntentIntegrator integrator = new IntentIntegrator(ValidateCustomer.this.getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        ValidateCustomer.this.getActivity().startActivityForResult(integrator.createScanIntent(), 1);
    }


}
